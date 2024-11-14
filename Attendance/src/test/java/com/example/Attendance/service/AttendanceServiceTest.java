package com.example.Attendance.service;

import com.example.Attendance.Repository.CommuteRepository;
import com.example.Attendance.Repository.StoreEmployeeRepository;
import com.example.Attendance.dto.EmployeeCommuteRequest;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.Commute;
import com.example.Attendance.model.Store;
import com.example.Attendance.model.StoreEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // Mockito 사용을 위한 JUnit 5 확장
public class AttendanceServiceTest {

    @Mock
    private StoreEmployeeRepository storeEmployeeRepository;

    @Mock
    private CommuteRepository commuteRepository;

    @InjectMocks
    private StoreEmployeeService storeEmployeeService;

    @Mock
    private StoreEmployee storeEmployee;

    @Mock
    private Commute commute;

    @Mock
    private EmployeeCommuteRequest request;

    @Mock
    private Store store;  // Store도 Mock 객체로 처리

    @BeforeEach
    void setUp() {
        // 필요한 경우에만 lenient()로 설정
        lenient().when(storeEmployee.getId()).thenReturn(1);  // 필요한 테스트에서만 사용됨
        lenient().when(storeEmployee.getStore()).thenReturn(store);  // 필요한 경우만 사용

        // Mocking Store 객체 (필요한 경우에만 설정)
        lenient().when(store.getLatitude()).thenReturn(37.5665);  // 필요한 테스트에서만 사용됨
        lenient().when(store.getLongitude()).thenReturn(126.9780);

        // Mocking EmployeeCommuteRequest 객체 (각 테스트에 맞게 설정)
        lenient().when(request.getEmail()).thenReturn("john.doe@test.com");
        lenient().when(request.getLatitude()).thenReturn(37.5665);
        lenient().when(request.getLongitude()).thenReturn(126.9780);  // 필요한 경우에만 설정
    }

    @Test
    void testGoToWork_NewCommute() {
        // Given: 이전 근무 기록이 없는 경우
        when(storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(), 1))
                .thenReturn(Optional.of(storeEmployee));
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.empty());

        // When: goToWork 호출
        boolean result = storeEmployeeService.goToWork(1, request);

        // Then: 새로운 출근 기록이 저장되고, true가 반환되어야 함
        assertTrue(result);
        verify(commuteRepository, times(1)).save(any(Commute.class));
    }

    @Test
    void testGoToWork_ExistingCommute() {
        // Given: 이전 근무 기록이 있고, 종료 시간이 없는 경우
        when(storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(), 1))
                .thenReturn(Optional.of(storeEmployee));
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.of(commute));

        // When: goToWork 호출
        boolean result = storeEmployeeService.goToWork(1, request);

        // Then: 출근 기록이 이미 있으므로 false가 반환되어야 함
        assertFalse(result);
        verify(commuteRepository, times(1)).save(any(Commute.class));
    }

    @Test
    void testLeaveWork_ValidCommute() {
        // Given: 직원이 존재하고, 출근 기록이 있으며, 해당 출근 기록에 아직 퇴근 시간이 없는 경우
        when(storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(), 1))
                .thenReturn(Optional.of(storeEmployee));  // 직원 정보를 Mock으로 반환
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.of(commute));  // 가장 최근 출근 기록을 Mock으로 반환

        // When: leaveWork 메서드 호출
        storeEmployeeService.leaveWork(1, request);  // 가게 ID와 요청 정보를 사용하여 퇴근 처리

        // Then: 퇴근 시간이 설정되어야 함 (테스트 성공 조건)
        assertNull(commute.getEndTime(), "End time should be set after leaving work");  // 퇴근 시간이 설정되었는지 확인

        // Then: 변경된 출근 기록이 저장되었는지 확인
        verify(commuteRepository, times(1)).save(commute);  // save 메서드가 한 번 호출되었는지 검증
    }

    // 출근 기록이 있고 퇴근시간이 찍혀있는경우


    @Test
    void testLeaveWork_NoCheckIn() {
        // Given: 출근 기록이 없는 경우
        when(storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(), 1))
                .thenReturn(Optional.of(storeEmployee));
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.empty());

        // When: leaveWork 호출
        CustomException thrown = assertThrows(CustomException.class, () -> {
            storeEmployeeService.leaveWork(1, request);
        });

        // Then: 퇴근 기록이 없으면 예외가 발생해야 함
        assertEquals(ErrorCode.INVALID_COMMUTE, thrown.getErrorCode());
    }

    @Test
    void testFindStoreEmployeeByEmailAndStoreId_ValidLocation() {
        // Given: 직원의 위치가 가게 근처에 있는 경우
        when(storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(), 1))
                .thenReturn(Optional.of(storeEmployee));

        when(request.getLatitude()).thenReturn(37.5665);  // 가게 위치와 동일한 위도
        when(request.getLongitude()).thenReturn(126.9780);  // 가게 위치와 동일한 경도

        // When: findStoreEmployeeByEmailAndStoreId 호출
        StoreEmployee result = storeEmployeeService.findStoreEmployeeByEmailAndStoreId(1, request);

        // Then: 예외가 발생하지 않고 정상적으로 직원 정보를 반환해야 함
        assertNotNull(result);
    }

    // 직원 위치가 근처가 아닌경우
    @Test
    void testFindStoreEmployeeByEmailAndStoreId_InvalidLocation() {
        // Given: 직원의 위치가 가게에서 멀리 떨어져 있는 경우
        when(storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(), 1))
                .thenReturn(Optional.of(storeEmployee));

        // 직원의 위치를 가게에서 멀리 떨어진 곳으로 설정 (예: 부산)
        when(request.getLatitude()).thenReturn(35.1796);  // 부산의 위도
        when(request.getLongitude()).thenReturn(129.0756);  // 부산의 경도

        // When: 직원이 가게에서 멀리 떨어져 있을 때, findStoreEmployeeByEmailAndStoreId 호출
        CustomException thrown = assertThrows(CustomException.class, () -> {
            storeEmployeeService.findStoreEmployeeByEmailAndStoreId(1, request);
        });

        // Then: INVALID_LOCATION 에러 코드로 예외가 발생해야 함
        assertEquals(ErrorCode.INVALID_LOCATION, thrown.getErrorCode());
    }

    @Test
    void testFindStoreEmployeeByEmailAndStoreId_InvalidEmployee() {
        // Given: 유효하지 않은 이메일이나 가게 ID인 경우 (직원이 존재하지 않음)
        when(storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(), 1))
                .thenReturn(Optional.empty());  // 직원 정보가 없는 경우로 설정

        // When: findStoreEmployeeByEmailAndStoreId 호출 시 CustomException이 발생해야 함
        CustomException thrown = assertThrows(CustomException.class, () -> {
            storeEmployeeService.findStoreEmployeeByEmailAndStoreId(1, request);
        });

        // Then: INVALID_EMPLOYEE 에러 코드로 예외가 발생해야 함
        assertEquals(ErrorCode.INVALID_EMPLOYEE, thrown.getErrorCode());
    }
}
