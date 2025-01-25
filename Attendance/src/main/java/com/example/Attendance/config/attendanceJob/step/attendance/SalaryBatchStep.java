package com.example.Attendance.config.attendanceJob.step.attendance;

import com.example.Attendance.dto.batch.*;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.error.ErrorDTO;
import com.example.Attendance.error.FeignExceptionHandler;
import com.example.Attendance.error.log.ErrorType;
import com.example.Attendance.feign.FeignWithCoreBank;
import com.example.Attendance.model.Batch;
import com.example.Attendance.repository.BatchRepository;
import com.example.Attendance.service.CommuteService;
import com.example.Attendance.service.StoreEmployeeService;
import com.example.Attendance.service.batch.BatchService;
import com.example.Attendance.service.batch.CalculateService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor

public class SalaryBatchStep {

    private final SalaryBatchState salaryBatchState;
    private final StoreEmployeeService storeEmployeeService;
    private final CommuteService commuteService;
    private final BatchService batchService;
    private final FeignWithCoreBank feignWithCoreBank;
    private final CalculateService calculateService;
    private final FeignExceptionHandler handler;

    @Bean("salaryReader")  // 빈 등록할 때 특정 이름을 지어줌 ,why? reader가 많으니까 동일한 bean이 여러개로 인식해서
    public ItemReader<BatchInputData> salaryReader() {
        return () -> {
            // state 즉 초기화 된상태면 직원찾고 일한 날짜 찾음
            try {
                if (salaryBatchState.getEmployees() == null) {
                    if (!salaryBatchState.setEmployees(storeEmployeeService.
                            findStoreEmployeeByTypeAndPaymentDate(salaryBatchState.getPaymentDay()))) {
                        return null;
                    }
                    salaryBatchState.setCommutes(commuteService.
                            findAllByCommuteDateBetween(salaryBatchState.getEmployeeIds(),
                                    salaryBatchState.getLocalDate()));
                }
                return salaryBatchState.findBatchInputData();
            } catch (Exception e) {
                log.error("데이터 읽기 실패: {}", e.getMessage(), e);
                throw new CustomException(ErrorCode.API_SERVER_ERROR);
            }
        };
    }

    @Bean("salaryProcessor")
    public ItemProcessor<BatchInputData, BatchOutputData> salaryProcessor() {
        //ItemProcessor는 앞에서 만든 객체(BatchInputData)를 하나씩 여기서 처리해서 결과값(BatchOutputData)를 다음 단계로 하나씩 넘겨줌
        return item -> { // item은  BatchInputData 뜻함
            try {
                // 사실 여기 부분때문에 state객체를 만들었는데,reader 단계의 일한 날짜 리스트를 직접 processor에 넘겨주기 어려워  state 객체를 만듦
                CommuteSummary commuteSummary = salaryBatchState.getCommuteDuration(item.getSeId());
                //여기 들어오기전에 0인애들 필터링 하면 좋을 듯
                if (commuteSummary == null) {
                    return null;
                }
                // 계산부분<- 여기는 그냥 무시하셈 너무 복잡함
                calculateService.calculate(item, commuteSummary);

                //feign을 위한 dto객체 생성
                TransferRequest request = TransferRequest.from(item); // 실급여
                TransferRequest adminRequest = TransferRequest.fromForAdmin(request); // 우리앱 수수료

                TransferResponse response = feignWithCoreBank.automaticTransfer(request); // 자동이체

                // 수수료 이체 진행하며 isCharge는 수수료 성공 여부
                try {
                    TransferResponse responseAdmin = feignWithCoreBank.automaticTransfer(adminRequest);
                    return BatchOutputData.of(response, item,true);
                } catch (FeignException fe) {
                    // 수수료 이체 실패 시 로깅 및 알림
                    log.error("수수료 이체 실패 - amount={}, error={}", adminRequest.getAmount(), fe.getMessage());

                    // 직원급여는 정상 처리된 것으로 처리
                    return BatchOutputData.of(response, item,false);
                }
            } catch (FeignException fe) {
                log.error("금융서버 통신 실패 - president_account={}, employee_account={}, error={}, type={}",
                        item.getFromAccount(), item.getToAccount(), fe.getMessage(), ErrorType.FEIGN_EXCEPTION.name());
                ErrorDTO dto =  handler.feToErrorDTO(fe);
                return BatchOutputData.ofFail(item,dto.getCode());

            } catch (Exception e) {
                log.error("자동이체 처리 실패 - president_account={}, employee_account={}, error={}, type={}",
                        item.getFromAccount(), item.getToAccount(), e.getMessage(), ErrorType.INTERNAL_ERROR.name());
                return BatchOutputData.ofFail(item,"서버 오류");
            }
        };
    }


    //processor 단계 item을 하나씩 모아 config에서 정한 chunk단위로 작동함
    //이 단계에서는 db에 쓰기 진행
    @Bean("salaryWriter")
    public ItemWriter<BatchOutputData> salaryWriter() {
        return chunk -> {
            List<Batch> batches= chunk.getItems().stream()
                    .map(BatchOutputData::ToBatchEntity)
                    .toList();
            batchService.saveAll(batches);

            //여기는 퇴사자 한정해서 처리
            List<Integer> ids= chunk.getItems().stream()
                    .filter(BatchOutputData::getIsMask)
                    .map(BatchOutputData::getSeId).toList();
            storeEmployeeService.updateEmployeeType(ids);
            log.info("급여 이체 결과 {} 건 저장 완료", chunk.size());
        };
    }
}
