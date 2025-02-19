package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Status;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class FileUserServiceTest {
    @Mock
    private FileUserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @InjectMocks
    private FileUserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유저를 생성한다.")
    void testCreateUser() {
        // given
        // Set user
        String userName1 = "username1";
        String userName2 = "username2";
        String userEmail1 = "email1@email.com";
        String userEmail2 = "email2@email.com";
        User user1 = new User(userName1, userEmail1);
        User user2 = new User(userName2, userEmail2);

        // Set binaryContent
        String fileName1 = "filename1";
        String fileName2 = "filename2";
        String fileMimeType1 = "mimetype1";
        String fileMimeType2 = "mimetype2";
        String filePath1 = "filePath1";
        String filePath2 = "filePath2";
        BinaryContent binaryContent1 = new BinaryContent(fileName1, fileMimeType1, filePath1);
        BinaryContent binaryContent2 = new BinaryContent(fileName2, fileMimeType2, filePath2);

        CreateUserRequest request1 = new CreateUserRequest(userName1, userEmail1, binaryContent1);
        CreateUserRequest request2 = new CreateUserRequest(userName2, userEmail2, binaryContent2);

        // when
        UserResponse savedUser1 = userService.createUser(request1);
        UserResponse savedUser2 = userService.createUser(request2);

        // then
        assertThat(savedUser1).isNotNull();
        assertThat(savedUser1.username()).isEqualTo(userName1);
        assertThat(savedUser1.email()).isEqualTo(userEmail1);
        assertThat(savedUser1.profileImage()).isEqualTo(binaryContent1);

        assertThat(savedUser2).isNotNull();
        assertThat(savedUser2.username()).isEqualTo(userName2);
        assertThat(savedUser2.email()).isEqualTo(userEmail2);
        assertThat(savedUser2.profileImage()).isEqualTo(binaryContent2);

        assertThat(savedUser1.status()).isNotNull();
        assertThat(savedUser2.status()).isNotNull();

        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    @DisplayName("유저를 생성하고 단일 조회를 한다")
    void testGetUserById() {
        // given
        // set user
        String userName1 = "username1";
        String userEmail1 = "email1@email.com";
        User user1 = new User(userName1, userEmail1);

        // Set binaryContent
        String fileName1 = "filename1";
        String fileMimeType1 = "mimetype1";
        String filePath1 = "filePath1";
        BinaryContent binaryContent1 = new BinaryContent(fileName1, fileMimeType1, filePath1);
        user1.updateProfileImage(binaryContent1);

        CreateUserRequest request1 = new CreateUserRequest(userName1, userEmail1, binaryContent1);

        // when
        when(userRepository.existsByUsername(userName1)).thenReturn(false);
        when(userRepository.existsByEmail(userEmail1)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(userRepository.getUserById(user1.getId())).thenReturn(user1);

        UserResponse savedUser1 = userService.createUser(request1);
        Optional<UserResponse> findUser1 = userService.findUserById(user1.getId());

        // then
        assertThat(findUser1).isPresent();
        assertThat(findUser1.get().username()).isEqualTo(userName1);
        assertThat(findUser1.get().email()).isEqualTo(userEmail1);
        assertThat(findUser1.get().profileImage()).isEqualTo(binaryContent1);
    }

    @Test
    @DisplayName("유저를 생성하고 업데이트한 후 조회한다")
    void testUpdateUser() {
        // given
        // set user
        String userName1 = "username1";
        String userEmail1 = "email1@email.com";
        User user1 = new User(userName1, userEmail1);

        // Set binaryContent
        String fileName1 = "filename1";
        String fileMimeType1 = "mimetype1";
        String filePath1 = "filePath1";
        BinaryContent binaryContent1 = new BinaryContent(fileName1, fileMimeType1, filePath1);
        user1.updateProfileImage(binaryContent1);

        CreateUserRequest request1 = new CreateUserRequest(userName1, userEmail1, binaryContent1);

        // set updateUser
        String updateUsername1 = "updateUsername1";
        String UpdateFilename1 = "UpdateFilename1";
        BinaryContent updateBinaryContent1 = new BinaryContent(UpdateFilename1, fileMimeType1, filePath1);

        UpdateUserRequest request2 = new UpdateUserRequest(user1.getId(), updateUsername1, updateBinaryContent1);

        // when
        when(userRepository.existsByUsername(userName1)).thenReturn(false);
        when(userRepository.existsByEmail(userEmail1)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(userRepository.getUserById(user1.getId())).thenReturn(user1);

        UserResponse savedUser1 = userService.createUser(request1);
        Optional<UserResponse> updatedUser1 = userService.updateUser(request2);
        Optional<UserResponse> findUser1 = userService.findUserById(user1.getId());

        // then
        assertThat(findUser1).isPresent();
        assertThat(findUser1.get().username()).isEqualTo(updateUsername1);
        assertThat(findUser1.get().profileImage()).isEqualTo(updateBinaryContent1);
    }

    @Test
    @DisplayName("유저를 생성하고 삭제한 후 확인한다")
    void testDeleteUser() {
        // given
        // set user
        String userName1 = "username1";
        String userEmail1 = "email1@email.com";
        User user1 = new User(userName1, userEmail1);

        // Set binaryContent
        String fileName1 = "filename1";
        String fileMimeType1 = "mimetype1";
        String filePath1 = "filePath1";
        BinaryContent binaryContent1 = new BinaryContent(fileName1, fileMimeType1, filePath1);
        user1.updateProfileImage(binaryContent1);

        // Set UserStatus
        UserStatus userStatus = new UserStatus(user1.getId());
        userStatus.updateStatus(Status.CONNECTED);
        user1.updateUserStatus(userStatus);

        CreateUserRequest request1 = new CreateUserRequest(userName1, userEmail1, binaryContent1);

        // when - 유저 생성 후 조회
        when(userRepository.existsByUsername(userName1)).thenReturn(false);
        when(userRepository.existsByEmail(userEmail1)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(userRepository.getUserById(user1.getId())).thenReturn(user1);
        UserResponse savedUser1 = userService.createUser(request1);
        Optional<UserResponse> findUser1 = userService.findUserById(user1.getId());

        // then - 유저가 정상적으로 생성되었는지 확인
        assertThat(findUser1).isPresent();
        assertThat(findUser1.get().username()).isEqualTo(userName1);
        assertThat(findUser1.get().email()).isEqualTo(userEmail1);
        assertThat(findUser1.get().profileImage()).isEqualTo(binaryContent1);
        assertThat(findUser1.get().status()).isNotNull();

        // when - 유저 삭제 수행
        userService.deleteUser(user1.getId());
        when(userRepository.getUserById(user1.getId())).thenReturn(null); // 삭제 후 조회 시 null 반환

        // then - 유저가 삭제되었는지 확인
        Optional<UserResponse> deletedUser = userService.findUserById(user1.getId());
        assertThat(deletedUser).isEmpty();

        // then - 연관 데이터도 삭제되었는지 검증
        verify(userRepository, times(1)).delete(user1.getId());
        verify(binaryContentRepository, times(1)).delete(user1.getProfileImage());
        verify(userStatusRepository, times(1)).deleteByUserId(user1.getId());
    }

    @Test
    @DisplayName("유저를 생성하는데 중복된 이름이 들어온다")
    void testCreateUserByDuplicateName() {
        // given
        // set user
        String userName1 = "username1";
        String userEmail1 = "email1@email.com";
        User user1 = new User(userName1, userEmail1);

        // Set binaryContent
        String fileName1 = "filename1";
        String fileMimeType1 = "mimetype1";
        String filePath1 = "filePath1";
        BinaryContent binaryContent1 = new BinaryContent(fileName1, fileMimeType1, filePath1);
        user1.updateProfileImage(binaryContent1);

        // Set UserStatus
        UserStatus userStatus = new UserStatus(user1.getId());
        userStatus.updateStatus(Status.CONNECTED);
        user1.updateUserStatus(userStatus);

        CreateUserRequest request1 = new CreateUserRequest(userName1, userEmail1, binaryContent1);

        // when
        when(userRepository.existsByUsername(userName1)).thenReturn(true);
        when(userRepository.existsByEmail(userEmail1)).thenReturn(true);

        // then
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(request1)
        );
        assertThat(exception.getMessage()).isEqualTo("이미 사용 중인 username 또는 email입니다.");
        verify(userRepository, never()).save(any(User.class));
    }
}
