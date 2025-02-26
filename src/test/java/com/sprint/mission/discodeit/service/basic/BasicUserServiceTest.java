//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.status.user.UserStatus;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.entity.user.UserCommonRequest;
//import com.sprint.mission.discodeit.entity.user.UserResponse;
//import com.sprint.mission.discodeit.entity.user.UserRole;
//import com.sprint.mission.discodeit.exception.user.IllegalUserException;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.BinaryContentService;
//import com.sprint.mission.discodeit.service.status.UserStateService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BasicUserServiceTest {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private UserStateService userStateService;
//    @Mock
//    private BinaryContentService binaryContentService;
//
//    @InjectMocks
//    private BasicUserService userService;
//
//    private User user1;
//    private User user2;
//    private UserCommonRequest userRequest1;
//    private UserStatus userStatus1;
//    private BinaryContent profileImage;
//    private Message message;
//    private Map<UUID, User> userMap;
//
//    @BeforeEach
//    void setUp() {
//        user1 = new User("userA", "test@gmail.com", "password", UserRole.ROLE_COMMON);
//        user2 = new User("userB", "test2@gmail.com", "password", UserRole.ROLE_COMMON);
//        userRequest1 = new UserCommonRequest(user1.getUserName(), user1.getUserEmail(), user1.getUserPassword());
//        userStatus1 = new UserStatus(user1.getId());
//        message = new Message("title", "content", user1, user2);
//        profileImage = new BinaryContent(user1.getId(), message.getId(), "profileImg", "jpg");
//
//        userMap = new HashMap<>();
//        userMap.put(user1.getId(), user1);
//    }
//
//    @Test
//    @DisplayName("유저 생성 성공")
//    void createUserSuccess() {
//        when(userRepository.findAllUser()).thenReturn(new HashMap<>());
//        when(userRepository.userSave(any(User.class))).thenReturn(user1);
//        when(userStateService.create(any())).thenReturn(userStatus1);
//
//        User createdUser = userService.createUser(userRequest1);
//
//        assertThat(createdUser.getUserName()).isEqualTo(user1.getUserName());
//        assertThat(createdUser.getUserEmail()).isEqualTo(user1.getUserEmail());
//        verify(userRepository, times(1)).userSave(any(User.class));
//        verify(userStateService, times(1)).create(any());
//    }
//
//    @Test
//    @DisplayName("프로필 이미지와 함께 유저 생성 성공")
//    void createUserWithProfileSuccess() {
//        when(userRepository.findAllUser()).thenReturn(new HashMap<>());
//        when(userRepository.userSave(any(User.class))).thenReturn(user1);
//        when(userStateService.create(any())).thenReturn(userStatus1);
//        when(binaryContentService.create(any())).thenReturn(profileImage);
//
//        User userWithProfile = userService.createUserWithProfile(userRequest1, profileImage);
//
//        assertThat(userWithProfile.getUserName()).isEqualTo(user1.getUserName());
//        assertThat(userWithProfile.getUserEmail()).isEqualTo(user1.getUserEmail());
//        verify(binaryContentService, times(1)).create(any());
//    }
//
//    @Test
//    @DisplayName("프로필 이미지와 함께 유저 생성 실패 - null 이미지")
//    void createUserWithProfileFailNullImage() {
//        assertThatThrownBy(() -> userService.createUserWithProfile(userRequest1, null))
//                .isInstanceOf(IllegalUserException.class)
//                .hasMessage("프로필 이미지 등록 오류: null");
//    }
//
//    @Test
//    @DisplayName("ID로 유저 조회 성공")
//    void getUserByIdSuccess() {
//        when(userRepository.findUserById(user1.getId())).thenReturn(user1);
//        when(userStateService.find(user1.getId())).thenReturn(userStatus1);
//
//        UserResponse response = userService.find(user1.getId());
//
//        assertThat(response.getUserName()).isEqualTo(user1.getUserName());
//        assertThat(response.getUserEmail()).isEqualTo(user1.getUserEmail());
//    }
//
//    @Test
//    @DisplayName("유저 정보 업데이트 성공")
//    void updateUserSuccess() {
//        UserCommonRequest updateRequest = new UserCommonRequest("updatedName", "updated@email.com", "newPassword");
//        when(userRepository.findUserById(any(UUID.class))).thenReturn(user1);
//        when(userStateService.updateByUserId(any(UUID.class))).thenReturn(userStatus1);
//        when(userRepository.userSave(any(User.class))).thenReturn(user1);
//
//        User updatedUser = userService.update(user1.getId(), updateRequest);
//
//        assertThat(updatedUser).isNotNull();
//        verify(userStateService, times(1)).updateByUserId(any(UUID.class));
//        verify(userRepository, times(1)).userSave(any(User.class));
//    }
//
//    @Test
//    @DisplayName("모든 유저 조회 성공")
//    void getAllUsersSuccess() {
//        Map<UUID, User> allUsers = new HashMap<>();
//        allUsers.put(user1.getId(), user1);
//        allUsers.put(user2.getId(), user2);
//
//        when(userRepository.findAllUser()).thenReturn(allUsers);
//        when(userStateService.find(any(UUID.class))).thenReturn(userStatus1);
//
//        Map<UUID, UserResponse> responses = userService.findAll();
//
//        assertThat(responses).hasSize(2);
//        verify(userRepository, times(1)).findAllUser();
//    }
//
//    @Test
//    @DisplayName("유저 삭제 성공")
//    void deleteUserSuccess() {
//        userService.deleteUser(user1.getId());
//
//        verify(userRepository, times(1)).removeUserById(user1.getId());
//        verify(userStateService, times(1)).delete(user1.getId());
//        verify(binaryContentService, times(1)).delete(user1.getId());
//    }
//
//    @Test
//    @DisplayName("유저 삭제 실패 - null ID")
//    void deleteUserFailWithNullId() {
//        assertThatThrownBy(() -> userService.deleteUser(null))
//                .isInstanceOf(IllegalUserException.class)
//                .hasMessage("userId를 다시 확인해주세요.");
//
//        verify(userRepository, never()).removeUserById(any());
//    }
//}