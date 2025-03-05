//package com.sprint.mission.discodeit.service;
//
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.repository.UserStatusRepository;
//import com.sprint.mission.discodeit.basic.BasicUserStatusService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserStatusServiceTest {
//
//    @Mock
//    private UserStatusRepository userStatusRepository;
//
//    @InjectMocks
//    private BasicUserStatusService userStatusService;
//
//    private UserStatus userStatus;
//
//    @BeforeEach
//    void setUp() {
//        userStatus = new UserStatus("testUser");
//    }
//
//    @Test
//    void testUpdateUserStatus() {
//        when(userStatusRepository.findByUserId("testUser")).thenReturn(Optional.of(userStatus));
//        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(userStatus);
//
//        UserStatus updatedStatus = userStatusService.updateUserStatus("testUser");
//
//        assertNotNull(updatedStatus);
//        assertTrue(updatedStatus.isOnline());
//        verify(userStatusRepository, times(1)).save(any(UserStatus.class));
//    }
//
//    @Test
//    void testIsUserOnline() {
//        when(userStatusRepository.findByUserId("testUser")).thenReturn(Optional.of(userStatus));
//
//        boolean isOnline = userStatusService.isUserOnline("testUser");
//
//        assertTrue(isOnline);
//    }
//}