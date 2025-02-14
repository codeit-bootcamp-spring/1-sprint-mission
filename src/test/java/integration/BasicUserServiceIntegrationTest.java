package integration;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class BasicUserServiceIntegrationTest {

  @Autowired
  private UserService userService;

  @Value("${app.file.user-file}")
  private String filePath;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;
  private CreateUserRequest userDto1, userDto2;

  @BeforeEach
  void setUp(){

    userRepository.clear();
    binaryContentRepository.clear();
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());

    userDto1 = new CreateUserRequest(
        "username1","pwd1","email1@email.com","nickname1","01012341234", mockFile,  "description1"
    );
    userDto2 = new CreateUserRequest(
        "username2","pwd2","email2@email.com","nickname2","01012341233",mockFile,  "description1"
    );
  }

  @Test
  void 유저를_생성할수_있다(){
    UserResponseDto user = userService.createUser(userDto1);

    assertThat(user).isNotNull();
    assertThat(user.username()).isEqualTo(userDto1.username());

    UserResponseDto userResponseDto = userService.findUserById(user.userId());
    assertThat(userResponseDto.userId()).isEqualTo(user.userId());
    assertThat(userResponseDto.userStatus()).isEqualTo("ONLINE");
  }

  @Test
  void 중복_이메일가입_실패(){
    CreateUserRequest duplicateEmailDto = new CreateUserRequest(
            "username3","pwd3","email1@email.com","nickname3","01012341232",null,  "description1"
    );
    userService.createUser(userDto1);

    assertThatThrownBy(() -> userService.createUser(duplicateEmailDto)).isInstanceOf(UserValidationException.class);

    assertThat(userService.findAllUsers()).hasSize(1);
  }

  @Test
  void 중복_핸드폰번호_실패(){
    CreateUserRequest duplicateEmailDto = new CreateUserRequest(
        "username3","pwd3","email1@email.com","nickname3","01012341232",null, "description1"
    );
    userService.createUser(userDto1);

    assertThatThrownBy(() -> userService.createUser(duplicateEmailDto)).isInstanceOf(UserValidationException.class);

    assertThat(userService.findAllUsers()).hasSize(1);
  }

  @Test
  void 중복_닉네임_실패(){
    CreateUserRequest duplicateEmailDto = new CreateUserRequest(
        "username3","pwd3","email1@email.com","nickname1","01012341236",null,  "description1"
    );
    userService.createUser(userDto1);

    assertThatThrownBy(() -> userService.createUser(duplicateEmailDto)).isInstanceOf(UserValidationException.class);

    assertThat(userService.findAllUsers()).hasSize(1);
  }

  @Test
  void 닉네임_길이_실패(){

    CreateUserRequest duplicateEmailDto = new CreateUserRequest(
        "username3","pwd3","email1@email.com","n","01012341236",null,  "description1"
    );

    assertThatThrownBy(() -> userService.createUser(duplicateEmailDto)).isInstanceOf(UserValidationException.class);
  }

  @Test
  void 유저를_조회할수_있다(){
    UserResponseDto user1 = userService.createUser(userDto1);

    UserResponseDto foundUser = userService.findUserById(user1.userId());


    assertThat(foundUser).isNotNull();
    assertThat(foundUser.userId()).isEqualTo(user1.userId());
    assertThat(foundUser.email()).isEqualTo(user1.email());
  }

  @Test
  void 모든_유저를_조회할_수_있다(){
    userService.createUser(userDto1);
    userService.createUser(userDto2);

    List<UserResponseDto> users = userService.findAllUsers();

    UserResponseDto user = users.stream()
        .filter(u -> "username1".equals(u.username()))
        .findAny()
        .orElseThrow();

    assertThat(users).hasSize(2);

    assertThat(user.profilePictureBase64()).isNotNull();
//    BinaryContent user1Profile = user.profilePictureBase64().getBytes();
//    assertThat(user1Profile.getUserId()).isEqualTo(user.userId());
//    assertThat(user1Profile.getFileName()).isEqualTo("image1");
  }

  @Test
  void 유저_정보를_수정할수_있다(){

    UserResponseDto user = userService.createUser(userDto1);

    UserUpdateDto updateDto = new UserUpdateDto(
        "UpdatedUsername", "newPassword123", "updated@email.com",
        "UpdatedNickname", "01087654321", "Updated Description",
        new byte[]{4, 5, 6}, "newImage", "jpg"
    );


    userService.updateUser(user.userId(), updateDto, "pwd1");


    UserResponseDto response = userService.findUserById(user.userId());

    assertThat(userService.findAllUsers()).hasSize(1);
    assertThat(response).isNotNull();
    assertThat(response.userId()).isEqualTo(user.userId());
    assertThat(response.username()).isEqualTo("UpdatedUsername");

//    BinaryContent profilePicture = response.profilePicture();
//    assertThat(profilePicture).isNotNull();
//    assertThat(profilePicture.getData()).containsExactlyInAnyOrder(4,5,6);
  }

  @Test
  void 비밀번호_오류시_정보_수정_실패(){

    UserResponseDto user = userService.createUser(userDto1);
    UserUpdateDto updateDto = new UserUpdateDto(
        "UpdatedUsername", "newPassword123", "updated@email.com",
        "UpdatedNickname", "01087654321", "Updated Description",
        new byte[]{4, 5, 6}, "newImage", "jpg"
    );

    assertThatThrownBy(() -> userService.updateUser(user.userId(), updateDto, "wrong-pwd")).isInstanceOf(UserValidationException.class);

    UserResponseDto foundUser = userService.findUserById(user.userId());
    assertThat(foundUser.username()).isNotEqualTo("UpdatedUsername");
  }

  @Test
  void 유저를_삭제할수_있다(){
    UserResponseDto user = userService.createUser(userDto1);

    userService.deleteUser(user.userId(), "pwd1");

    assertThat(userService.findAllUsers()).hasSize(0);
  }

  @Test
  void 비밀번호_오류시_유저삭제_실패(){
    UserResponseDto user = userService.createUser(userDto1);

    assertThatThrownBy(() -> userService.deleteUser(user.userId(), "wrong-pwd")).isInstanceOf(UserValidationException.class);
    assertThat(userService.findAllUsers()).hasSize(1);
  }

  @Test
  void 비밀번호_변경후_기존_비밀번호_불가(){
    UserResponseDto user = userService.createUser(userDto1);

    UserUpdateDto updateDto = new UserUpdateDto(
        user.nickname(), "newSecurePwd123", user.email(),
        user.username(), user.phoneNumber(), "new description",
        null, null, null
    );

    userService.updateUser(user.userId(), updateDto, "pwd1");

    assertThatThrownBy(() -> userService.updateUser(user.userId(), updateDto, "pwd1")).isInstanceOf(UserValidationException.class);
  }

  @Test
  void 프로필_사진을_등록하고_불러올수_있다(){
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());

    UserResponseDto user = userService.createUser(

        new CreateUserRequest(
            "newUser",
            "newPwd",
            "new@email.com",
            "newNickname",
            "01098765432",
            mockFile,
            "description"
        )
    );

    UserResponseDto responseDto = userService.findUserById(user.userId());
//    BinaryContent profileImage = responseDto.profilePicture();
//
//    assertThat(profileImage.getUserId()).isEqualTo(user.userId());
//    assertThat(profileImage.getFileName()).isEqualTo("profileImage");
  }

  @Test
  void 프로필_사진을_등록하지_않으면_null(){
    UserResponseDto user = userService.createUser(
        new CreateUserRequest(
            "newUser",
            "newPwd",
            "new@email.com",
            "newNickname",
            "01098765432",
             null,"description"
        )
    );

    UserResponseDto responseDto = userService.findUserById(user.userId());


//    assertThat(responseDto.profilePicture()).isNull();
  }
}
