package com.example.Attendance.service;


import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.Commute;
import com.example.Attendance.model.StoreEmployee;
import com.example.Attendance.repository.CommuteRepository;
import com.example.Attendance.repository.StoreEmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // Mockito 사용을 위한 JUnit 5 확장
public class CommuteServiceTest {

    @Mock
    private StoreEmployeeRepository storeEmployeeRepository;

    @Mock
    private CommuteRepository commuteRepository;

    @InjectMocks
    private CommuteService commuteService;

    @Mock
    private StoreEmployee storeEmployee;

    @Mock
    private Commute commute;


    @Test
    void testGoToWork_NewCommute() {
        String email = "john.doe@test.com";
        // Given
        lenient().when(storeEmployeeRepository.findByEmailAndStoreId(email, 1))
                .thenReturn(Optional.of(storeEmployee));
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.empty());

        // When
        boolean result = commuteService.goToWork(storeEmployee);

        // Then
        assertTrue(result);
        verify(commuteRepository, times(1)).save(any(Commute.class));
    }


    @Test
    void testGoToWork_ExistingCommute() {
        String email = "john.doe@test.com";
        // Given: 이전 근무 기록이 있고, 종료 시간이 없는 경우
        lenient().when(storeEmployeeRepository.findByEmailAndStoreId(email, 1))
                .thenReturn(Optional.of(storeEmployee));
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.of(commute));

        // When: goToWork 호출
        boolean result = commuteService.goToWork(storeEmployee);

        // Then: 출근 기록이 이미 있으므로 false가 반환되어야 함
        assertFalse(result);
        verify(commuteRepository, times(1)).save(any(Commute.class));
    }

    @Test
    void testLeaveWork_ValidCommute() {
        String email = "john.doe@test.com";
        // Given
        lenient().when(storeEmployeeRepository.findByEmailAndStoreId(email, 1))
                .thenReturn(Optional.of(storeEmployee));
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.of(commute));

        // Commute 객체에 EndTime이 없는 상태로 설정
        when(commute.getEndTime()).thenReturn(null);

        // When
        commuteService.leaveWork(storeEmployee);

        // Then: 퇴근 시간이 설정되었는지 확인
        verify(commute).setEndTime(any(LocalDateTime.class)); // 퇴근 시간이 설정되었는지 확인
        verify(commuteRepository, times(1)).save(commute); // 저장 메서드가 호출되었는지 확인
    }

    // 출근 기록이 있고 퇴근시간이 찍혀있는경우
    @Test
    void testLeaveWork_AlreadyCheckOut() {
        String email = "john.doe@test.com";
        // Given
        lenient().when(storeEmployeeRepository.findByEmailAndStoreId(email, 1))
                .thenReturn(Optional.of(storeEmployee));
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.of(commute));

        // 이미 퇴근 시간이 설정된 경우
        when(commute.getEndTime()).thenReturn(LocalDateTime.now().minusHours(1));

        // When & Then: 이미 퇴근한 상태일 경우 예외 발생 확인
        CustomException thrown = assertThrows(CustomException.class, () -> {
            commuteService.leaveWork(storeEmployee);
        });

        assertEquals(ErrorCode.MISSING_GO_TO_WORK_RECODE, thrown.getErrorCode());
        verify(commuteRepository, never()).save(any(Commute.class)); // save 메서드는 호출되지 않아야 함
    }


    @Test
    void testLeaveWork_NoCheckIn() {
        String email = "john.doe@test.com";
        // Given: 출근 기록이 없는 경우
        lenient().when(storeEmployeeRepository.findByEmailAndStoreId(email, 1))
                .thenReturn(Optional.of(storeEmployee));
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.empty());

        // When: leaveWork 호출
        CustomException thrown = assertThrows(CustomException.class, () -> {
            commuteService.leaveWork(storeEmployee);
        });

        // Then: 퇴근 기록이 없으면 예외가 발생해야 함
        assertEquals(ErrorCode.INVALID_COMMUTE, thrown.getErrorCode());
    }
}
