package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.validation.ValidateUserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicUserStatusServiceTest {

    @InjectMocks
    BasicUserStatusService basicUserStatusService;

    @Mock
    UserStatusRepository userStatusRepository;

    @Mock
    ValidateUserStatus validateUserStatus;

    @Test
    void create_Success(){
        // given
        UUID userId = UUID.randomUUID();
        UserStatusCreateRequest request = new UserStatusCreateRequest(userId);
        UserStatus userStatus = new UserStatus(userId, Instant.now());

        // when
        doNothing().when(validateUserStatus).validateUserStatus(userId);
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(userStatus);

        UserStatusDto result = basicUserStatusService.create(request);

        // then
        assertNotNull(result);
        assertEquals(result.userId(), request.userId());

        verify(userStatusRepository, times(1)).save(any(UserStatus.class));
    }

    @Test
    void findByUserId_Success(){
        // given
        UUID userId = UUID.randomUUID();
        UserStatus userStatus = new UserStatus(userId, Instant.now());

        // when
        when(userStatusRepository.findByUserId(userId)).thenReturn(Optional.of(userStatus));

        UserStatusDto result = basicUserStatusService.findByUserId(userId);

        // then
        assertNotNull(result);
        assertEquals(result.userId(), userStatus.getUserId());

        verify(userStatusRepository, times(1)).findByUserId(userId);
    }

    @Test
    void findByUserId_UserStatusNotFound(){
        // given
        UUID userId = UUID.randomUUID();

        // when
        when(userStatusRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicUserStatusService.findByUserId(userId));
    }

    @Test
    void findAll_Success(){
        // given
        List<UserStatus> userStatuses = new ArrayList<>(List.of(
                new UserStatus(UUID.randomUUID(), Instant.now()),
                new UserStatus(UUID.randomUUID(), Instant.now())
        ));

        // when
        when(userStatusRepository.findAll()).thenReturn(userStatuses);

        List<UserStatusDto> results = basicUserStatusService.findAll();

        // then
        assertNotNull(results);
        assertEquals(2, results.size());

        verify(userStatusRepository, times(1)).findAll();
    }

    @Test
    void update_Success(){
        // given
        UserStatus userStatus = new UserStatus();
        User user = new User();
        UserStatusUpdateRequest request = new UserStatusUpdateRequest(userStatus.getId(), user.getUserId(), Instant.now());

        // when
        when(userStatusRepository.findByUserId(request.userId())).thenReturn(Optional.of(userStatus));
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(userStatus);

        UserStatusDto result = basicUserStatusService.update(request);

        // then
        assertNotNull(result);
        assertEquals(result.userStatusId(), request.userStatusId());

        verify(userStatusRepository, times(1)).findByUserId(request.userId());
        verify(userStatusRepository, times(1)).save(any(UserStatus.class));
    }

    @Test
    void update_UserStatusNotFound(){
        // given
        UserStatus userStatus = new UserStatus();
        User user = new User();
        UserStatusUpdateRequest request = new UserStatusUpdateRequest(userStatus.getId(), user.getUserId(), Instant.now());

        // when
        when(userStatusRepository.findByUserId(request.userId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicUserStatusService.update(request));

        verify(userStatusRepository, never()).save(any(UserStatus.class));
    }

    @Test
    void delete_Success(){
        // given
        UUID userId = UUID.randomUUID();

        // when
        basicUserStatusService.delete(userId);

        // then
        verify(userStatusRepository, times(1)).deleteByUserId(userId);
    }
}