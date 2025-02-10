package service;

import com.sprint.mission.discodeit.dto.user_status.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.UserStatusServiceImpl;
import com.sprint.mission.discodeit.validator.EntityValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserStatusServiceImplTest {

  @Mock
  private UserStatusRepository userStatusRepository;
  @Mock
  private EntityValidator validator;
  @InjectMocks
  private UserStatusServiceImpl userStatusService;

  private UserStatus status1, status2;

  @BeforeEach
  void setUp(){
    status1 = new UserStatus("user1", Instant.now());
    status2 = new UserStatus("user2", Instant.now());
  }

  @Test
  void testCreate_Success(){
    CreateUserStatusDto dto = new CreateUserStatusDto("user1", Instant.now());

    when(userStatusRepository.findByUserId("user1")).thenReturn(Optional.empty());
    when(userStatusRepository.save(any(UserStatus.class))).thenAnswer(status -> status.getArgument(0));

    UserStatus status = userStatusService.create(dto);

    assertThat(status).isNotNull();
    assertThat(status.getUserId()).isEqualTo("user1");

    verify(validator, times(1)).findOrThrow(eq(User.class), eq("user1"), any(UserNotFoundException.class));
    verify(userStatusRepository, times(1)).save(any(UserStatus.class));
  }

  @Test
  void testCreate_Fail_Duplicate(){
    CreateUserStatusDto dto = new CreateUserStatusDto("user1", Instant.now());
    when(userStatusRepository.findByUserId("user1")).thenReturn(Optional.ofNullable(status1));

    assertThatThrownBy(() -> userStatusService.create(dto)).isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void testFind_SuccessAndFail(){
    when(userStatusRepository.findById("user1")).thenReturn(Optional.ofNullable(status1));
    when(userStatusRepository.findById("wrongId")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userStatusService.find("wrongId")).isInstanceOf(InvalidOperationException.class);

    UserStatus status = userStatusService.find("user1");

    assertThat(status).isNotNull();
    assertThat(status.getUserId()).isEqualTo("user1");
  }

  @Test
  void testUpdate(){
    UpdateUserStatusDto dto = new UpdateUserStatusDto("user1_status_id", Instant.now());

    when(userStatusRepository.findById("user1_status_id")).thenReturn(Optional.ofNullable(status1));

    UserStatus updated = userStatusService.update(dto);

    assertThat(updated).isNotNull();

    verify(userStatusRepository, times(1)).findById(anyString());

  }

  @Test
  void testUpdate_NotFound(){
    UpdateUserStatusDto dto = new UpdateUserStatusDto("wrongId", Instant.now());

    when(userStatusRepository.findById("wrongId")).thenThrow(new InvalidOperationException(DEFAULT_ERROR_MESSAGE));

    assertThatThrownBy(() -> userStatusService.update(dto)).isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void testUpdateByUserId(){
    UpdateUserStatusDto dto = new UpdateUserStatusDto("id1", Instant.now());
    UserStatus spyStatus = spy(status1);

    when(userStatusRepository.findByUserId("user1")).thenReturn(Optional.ofNullable(spyStatus));
    when(userStatusRepository.save(any(UserStatus.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    UserStatus status = userStatusService.updateByUserId("user1", dto);

    assertThat(status).isNotNull();
    assertThat(status.getUserId()).isEqualTo("user1");

    verify(validator, times(1)  ).findOrThrow(eq(User.class), eq("user1"), any(UserNotFoundException.class));
    verify(userStatusRepository, times(1)).findByUserId("user1");
    verify(userStatusRepository, times(1)).save(any(UserStatus.class));
    verify(spyStatus, times(1)).updateLastOnline();
  }

  @Test
  void testUpdateByUserId_Fail(){
    when(userStatusRepository.findByUserId("user1")).thenReturn(Optional.empty());
    UpdateUserStatusDto dto = new UpdateUserStatusDto("id1", Instant.now());

    assertThatThrownBy(()-> userStatusService.updateByUserId("user1", dto)).isInstanceOf(InvalidOperationException.class);
    verify(userStatusRepository, never()).save(any(UserStatus.class));
  }
}
