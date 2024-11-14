package com.example.Attendance.service;

import com.example.Attendance.Repository.CommuteRepository;
import com.example.Attendance.Repository.StoreEmployeeRepository;
import com.example.Attendance.dto.EmployeeCommuteRequest;
import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.model.Commute;
import com.example.Attendance.model.StoreEmployee;
import com.example.Attendance.model.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Mockito 사용을 위한 JUnit 5 확장
public class AttendanceServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AttendanceServiceTest.class);
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
        // Mocking StoreEmployee 객체
        when(storeEmployee.getId()).thenReturn(1);
//        when(storeEmployee.getEmail()).thenReturn("john.doe@test.com");
//        when(storeEmployee.getName()).thenReturn("John Doe");
        when(storeEmployee.getStore()).thenReturn(store);  // Store 설정

        // Mocking Store 객체
        when(store.getLatitude()).thenReturn(37.5665);  // 예시 좌표
        when(store.getLongitude()).thenReturn(126.9780); // 예시 좌표

        // Mocking Commute 객체
//        when(commute.getId()).thenReturn(1);
//        when(commute.getStartTime()).thenReturn(LocalDateTime.now());
//        when(commute.getStoreEmployee()).thenReturn(storeEmployee);

        // Mocking EmployeeCommuteRequest 객체
        when(request.getEmail()).thenReturn("john.doe@test.com");
        when(request.getLatitude()).thenReturn(37.5665);
        when(request.getLongitude()).thenReturn(126.9780); // 서울 좌표

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
        // Given: 출근 기록이 있고, 종료 시간이 없는 경우
        when(storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(), 1))
                .thenReturn(Optional.of(storeEmployee));
        when(commuteRepository.findTopByStoreEmployeeIdOrderByStartTimeDesc(storeEmployee.getId()))
                .thenReturn(Optional.of(commute));
        log.info("commute : " + commute.getId());

        // When: leaveWork 호출
        storeEmployeeService.leaveWork(1, request);

        // Then: 퇴근 시간이 설정되어야 함
        log.info("commute: " +commute.getEndTime().toString());
        assertNotNull(commute.getEndTime(), "End time should be set after leaving work");
        verify(commuteRepository, times(1)).save(commute);
    }

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
    void testFindStoreEmployeeByEmailAndStoreId_InvalidLocation() {
        // Given: 직원의 위치가 잘못된 경우
        when(storeEmployeeRepository.findByEmailAndStoreId(request.getEmail(), 1))
                .thenReturn(Optional.of(storeEmployee));

        // When: findStoreEmployeeByEmailAndStoreId 호출
        CustomException thrown = assertThrows(CustomException.class, () -> {
            storeEmployeeService.findStoreEmployeeByEmailAndStoreId(1, request);
        });

        // Then: 잘못된 위치라면 예외가 발생해야 함
        assertEquals(ErrorCode.INVALID_LOCATION, thrown.getErrorCode());
    }
}
