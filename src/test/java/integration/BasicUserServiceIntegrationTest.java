package integration;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static com.sprint.mission.discodeit.constant.BinaryContentConstant.DEFAULT_PROFILE_PICTURE_NAME;
import static com.sprint.mission.discodeit.constant.BinaryContentConstant.DEFAULT_PROFILE_PICTURE_UUID;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class BasicUserServiceIntegrationTest {

  @Autowired
  private UserService userService;

  @Value("${app.file.user-file}")
  private String filePath;

  private CreateUserDto userDto1, userDto2;

  @BeforeEach
  void setUp(){

    File file = new File(filePath);
    if(file.exists()) file.delete();

    userDto1 = new CreateUserDto(
        "username1","pwd1","email1@email.com","nickname1","01012341234",new byte[]{1}, "image1", "jpg", "description1"
    );
    userDto2 = new CreateUserDto(
        "username2","pwd2","email2@email.com","nickname2","01012341233",new byte[]{1}, "image2", "jpg", "description1"
    );
  }

  @Test
  void 유저를_생성할수_있다(){
    User user = userService.createUser(userDto1);

    assertThat(user).isNotNull();
    assertThat(user.getPassword()).isNotEqualTo(userDto1.password());
    assertThat(user.getUsername()).isEqualTo(userDto1.username());

    UserResponseDto userResponseDto = userService.findUserById(user.getUUID());
    assertThat(userResponseDto.userId()).isEqualTo(user.getUUID());
    assertThat(userResponseDto.userStatus()).isEqualTo("ONLINE");
  }

  @Test
  void 중복_이메일가입_실패(){
    CreateUserDto duplicateEmailDto = new CreateUserDto(
            "username3","pwd3","email1@email.com","nickname3","01012341232",new byte[]{1}, "image3", "jpg", "description1"
    );
    userService.createUser(userDto1);

    assertThatThrownBy(() -> userService.createUser(duplicateEmailDto)).isInstanceOf(UserValidationException.class);

    assertThat(userService.findAllUsers()).hasSize(1);
  }

  @Test
  void 중복_핸드폰번호_실패(){
    CreateUserDto duplicateEmailDto = new CreateUserDto(
        "username3","pwd3","email3@email.com","nickname3","01012341234",new byte[]{1}, "image3", "jpg", "description1"
    );
    userService.createUser(userDto1);

    assertThatThrownBy(() -> userService.createUser(duplicateEmailDto)).isInstanceOf(UserValidationException.class);

    assertThat(userService.findAllUsers()).hasSize(1);
  }

  @Test
  void 중복_닉네임_실패(){
    CreateUserDto duplicateEmailDto = new CreateUserDto(
        "username3","pwd3","email1@email.com","nickname1","01012341236",new byte[]{1}, "image3", "jpg", "description1"
    );
    userService.createUser(userDto1);

    assertThatThrownBy(() -> userService.createUser(duplicateEmailDto)).isInstanceOf(UserValidationException.class);

    assertThat(userService.findAllUsers()).hasSize(1);
  }

  @Test
  void 닉네임_길이_실패(){
    CreateUserDto duplicateEmailDto = new CreateUserDto(
        "username3","pwd3","email1@email.com","n","01012341236",new byte[]{1}, "image3", "jpg", "description1"
    );

    assertThatThrownBy(() -> userService.createUser(duplicateEmailDto)).isInstanceOf(UserValidationException.class);
  }

  @Test
  void 유저를_조회할수_있다(){
    User user1 = userService.createUser(userDto1);

    UserResponseDto foundUser = userService.findUserById(user1.getUUID());

    assertThat(foundUser).isNotNull();
    assertThat(foundUser.userId()).isEqualTo(user1.getUUID());
    assertThat(foundUser.email()).isEqualTo(user1.getEmail());
  }

  @Test
  void 모든_유저를_조회할_수_있다(){
    userService.createUser(userDto1);
    userService.createUser(userDto2);

    List<UserResponseDto> users = userService.findAllUsers();

    assertThat(users).hasSize(2);
  }

  @Test
  void 유저_정보를_수정할수_있다(){

    User user = userService.createUser(userDto1);
    UserUpdateDto updateDto = new UserUpdateDto(
        "UpdatedUsername", "newPassword123", "updated@email.com",
        "UpdatedNickname", "01087654321", "Updated Description",
        new byte[]{4, 5, 6}, "newImage", "jpg"
    );


    userService.updateUser(user.getUUID(), updateDto, "pwd1");


    UserResponseDto response = userService.findUserById(user.getUUID());
    assertThat(userService.findAllUsers()).hasSize(1);
    assertThat(response).isNotNull();
    assertThat(response.userId()).isEqualTo(user.getUUID());
    assertThat(response.username()).isEqualTo("UpdatedUsername");
  }

  @Test
  void 비밀번호_오류시_정보_수정_실패(){

    User user = userService.createUser(userDto1);
    UserUpdateDto updateDto = new UserUpdateDto(
        "UpdatedUsername", "newPassword123", "updated@email.com",
        "UpdatedNickname", "01087654321", "Updated Description",
        new byte[]{4, 5, 6}, "newImage", "jpg"
    );

    assertThatThrownBy(() -> userService.updateUser(user.getUUID(), updateDto, "wrong-pwd")).isInstanceOf(UserValidationException.class);

    UserResponseDto foundUser = userService.findUserById(user.getUUID());
    assertThat(foundUser.username()).isNotEqualTo("UpdatedUsername");
  }

  @Test
  void 유저를_삭제할수_있다(){
    User user = userService.createUser(userDto1);

    userService.deleteUser(user.getUUID(), "pwd1");

    assertThat(userService.findAllUsers()).hasSize(0);
  }

  @Test
  void 비밀번호_오류시_유저삭제_실패(){
    User user = userService.createUser(userDto1);

    assertThatThrownBy(() -> userService.deleteUser(user.getUUID(), "wrong-pwd")).isInstanceOf(UserValidationException.class);
    assertThat(userService.findAllUsers()).hasSize(1);
  }

  @Test
  void 비밀번호_변경후_기존_비밀번호_불가(){
    User user = userService.createUser(userDto1);

    UserUpdateDto updateDto = new UserUpdateDto(
        user.getNickname(), "newSecurePwd123", user.getEmail(),
        user.getUsername(), user.getPhoneNumber(), user.getDescription(),
        null, null, null
    );

    userService.updateUser(user.getUUID(), updateDto, "pwd1");

    assertThatThrownBy(() -> userService.updateUser(user.getUUID(), updateDto, "pwd1")).isInstanceOf(UserValidationException.class);
  }

  @Test
  void 프로필_사진을_등록하고_불러올수_있다(){
    User user = userService.createUser(
        new CreateUserDto(
            "newUser",
            "newPwd",
            "new@email.com",
            "newNickname",
            "01098765432",
            new byte[]{1,2,3},
            "profileImage",
            "jpg","description"
        )
    );

    UserResponseDto responseDto = userService.findUserById(user.getUUID());
    BinaryContent profileImage = responseDto.profilePicture();

    assertThat(profileImage.getUserId()).isEqualTo(user.getUUID());
    assertThat(profileImage.getFileName()).isEqualTo("profileImage");
    assertThat(profileImage.getData()).hasSize(3);
    assertThat(profileImage.getData()).containsExactlyInAnyOrder(1,2,3);
  }

  @Test
  void 프로필_사진을_등록하지_않으면_null(){
    User user = userService.createUser(
        new CreateUserDto(
            "newUser",
            "newPwd",
            "new@email.com",
            "newNickname",
            "01098765432",
            null, null, null,"description"
        )
    );

    UserResponseDto responseDto = userService.findUserById(user.getUUID());


    assertThat(responseDto.profilePicture()).isNull();
  }
}
