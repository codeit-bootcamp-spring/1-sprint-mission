package com.sprint.mission.unit.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.basic.UserOnlineStatusService;
import com.sprint.mission.discodeit.service.facade.user.UserFacadeImpl;
import com.sprint.mission.discodeit.service.user.UserManagementService;
import com.sprint.mission.unit.TestEntityFactory;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
public class UserFacadeUnitTest {

  @Mock
  private UserManagementService userManagementService;
  @Mock
  private UserMapper userMapper;

  @Mock
  private PasswordEncoder encoder;
  @Mock
  private UserOnlineStatusService userOnlineStatusService;


  @InjectMocks
  private UserFacadeImpl userFacade;


  @Nested
  class CreateUser {

    @Test
    void createUser_shouldCallMapperAndServiceAndReturnResponse() {
      // given
      CreateUserRequest request = new CreateUserRequest("testUsername", "pwd",
          "test@example.com");
      MockMultipartFile mockProfile = new MockMultipartFile("profile", "test.jpg",
          "image/jpeg",

          "test".getBytes());
      User user = mock(User.class);
      User createdUser = mock(User.class);
      UserDto response = mock(UserDto.class);

      given(userMapper.toEntity(request, encoder)).willReturn(user);
      given(userManagementService.createUser(user, mockProfile)).willReturn(createdUser);
      given(userMapper.toDto(createdUser, userOnlineStatusService)).willReturn(response);

      // when
      UserDto result = userFacade.createUser(request, mockProfile);

      //then
      assertThat(result).isEqualTo(response);
      then(userMapper).should().toEntity(request, encoder);
      then(userManagementService).should().createUser(user, mockProfile);
      then(userMapper).should().toDto(createdUser, userOnlineStatusService);

    }

    @Test
    void createUser_shouldHaveNoMoreInteractionsOnMappingFail() {

      CreateUserRequest request = new CreateUserRequest("testUsername", "pwd",
          "test@example.com");
      MockMultipartFile mockProfile = new MockMultipartFile("profile", "test.jpg",
          "image/jpeg",
          "test".getBytes());

      given(userMapper.toEntity(request, encoder)).willThrow(new IllegalArgumentException());

      // when & then
      assertThatThrownBy(() -> userFacade.createUser(request, mockProfile)).isInstanceOf(
          IllegalArgumentException.class);
      then(userManagementService).shouldHaveNoInteractions();

      then(userMapper).should(times(0)).toDto(any(), any());

    }
  }

  @Nested
  class UpdateUser {

    @Test
    void updateUser_shouldCallMapperAndServiceAndReturnResponse() {
      String userId = UUID.randomUUID().toString();

      MockMultipartFile mockProfile = new MockMultipartFile("profile", "test.jpg",
          "image/jpeg",

          "test".getBytes());
      UserUpdateDto req = new UserUpdateDto("new", "new@gmail.com", "newPwd");
      User tmpUser = mock(User.class);
      User updatedUser = mock(User.class);
      UserDto res = mock(UserDto.class);
      UserDetails details = mock(DiscodeitUserDetails.class);
      given(userMapper.toEntity(req, encoder)).willReturn(tmpUser);
      given(userManagementService.updateUser(userId, tmpUser, mockProfile)).willReturn(
          updatedUser);
      given(userMapper.toDto(updatedUser, userOnlineStatusService)).willReturn(res);

      // when
      UserDto response = userFacade.updateUser(userId, mockProfile, req, details);

      //then
      assertThat(response).isEqualTo(res);
      then(userMapper).should().toEntity(req, encoder);
      then(userManagementService).should().updateUser(userId, tmpUser, mockProfile);
      then(userMapper).should().toDto(updatedUser, userOnlineStatusService);

    }

    @Test
    void updateUser_shouldHaveNoMoreInteractionsOnMappingFail() {
      //given
      String userId = UUID.randomUUID().toString();

      MockMultipartFile mockProfile = new MockMultipartFile("profile", "test.jpg",
          "image/jpeg",
          "test".getBytes());
      UserUpdateDto req = new UserUpdateDto("new", "new@gmail.com", "newPwd");
      UserDetails details = mock(DiscodeitUserDetails.class);
      given(userMapper.toEntity(req, encoder)).willThrow(new IllegalArgumentException());

      //when & then
      assertThatThrownBy(() -> userFacade.updateUser(userId, mockProfile, req, details))
          .isInstanceOf(IllegalArgumentException.class);
      then(userManagementService).shouldHaveNoInteractions();
      then(userMapper).should(times(0)).toDto(any(User.class), any());

    }
  }

  @Nested
  class FindUser {

    @Test
    void findUserById_shouldCall_success() {
      // given
      User user = TestEntityFactory.createUser("u", "u@gmail.com");
      String userId = user.getId().toString();
      UserDto response = mock(UserDto.class);

      given(userManagementService.findSingleUser(userId)).willReturn(user);
      given(userMapper.toDto(user, userOnlineStatusService)).willReturn(response);

      //when
      UserDto result = userFacade.findUserById(userId);

      // then
      assertThat(result).isEqualTo(response);
      then(userManagementService).should().findSingleUser(userId);
      then(userMapper).should().toDto(user, userOnlineStatusService);

    }

    @Test
    void findUserById_findSingleUser_throwsException() {
      // given
      String userId = UUID.randomUUID().toString();
      given(userManagementService.findSingleUser(userId))
          .willThrow(UserNotFoundException.class);

      //when & then
      assertThatThrownBy(() -> userFacade.findUserById(userId))
          .isInstanceOf(UserNotFoundException.class);
    }
  }

  @Nested
  class DeleteUser {

    @Test
    void deleteUser_shouldCallService() {
      // given
      String id = UUID.randomUUID().toString();

      //when
      userFacade.deleteUser(id);

      //then
      then(userManagementService).should().deleteUser(id);
    }
  }

}
