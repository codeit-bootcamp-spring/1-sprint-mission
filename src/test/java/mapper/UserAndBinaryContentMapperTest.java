package mapper;


import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.BinaryContentMapperImpl;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class UserAndBinaryContentMapperTest {
  @Autowired
  private UserMapper userMapper;

  @Autowired
  private BinaryContentMapper binaryContentMapper;
  User user;
  BinaryContent profile;
  UserStatus status;
  @BeforeEach
  void setUp(){
    MockMultipartFile file = new MockMultipartFile(
        "profileImage", "test.jpg", "image/jpeg", "testImageData".getBytes()
    );

    CreateUserRequest req = new CreateUserRequest(
        "username",
        "pwd1",
        "email@email.com",
        "nickname",
        "01012341234",
        file,
        "description"
    );


    user = userMapper.toEntity(req);

    profile = binaryContentMapper.toProfileBinaryContent(file, user.getUUID());

    status = new UserStatus(user.getUUID(), Instant.now());
  }

  @Test
  void testMapping() throws IOException {

    MockMultipartFile file = new MockMultipartFile(
        "profileImage", "test.jpg", "image/jpeg", "testImageData".getBytes()
    );

    CreateUserRequest req = new CreateUserRequest(
        "username",
        "pwd1",
        "email@email.com",
        "nickname",
        "01012341234",
        file,
        "description"
    );


    User user = userMapper.toEntity(req);
    BinaryContent content = binaryContentMapper.toProfileBinaryContent(file, user.getUUID());



    assertThat(user).isNotNull();
    assertThat(user.getUsername()).isEqualTo("username");
    assertThat(user.getPassword()).isNotEqualTo("pwd1");
    assertThat(user.getEmail()).isEqualTo("email@email.com");
    assertThat(user.getNickname()).isEqualTo("nickname");
    assertThat(user.getPhoneNumber()).isEqualTo("01012341234");
    assertThat(user.getDescription()).isEqualTo("description");

    assertThat(content).isNotNull();
    assertThat(content.getUserId()).isEqualTo(user.getUUID());
    assertThat(content.getData()).isEqualTo(
        file.getBytes()
    );
    assertThat(content.getMessageId()).isNull();
    assertThat(content.getChannelId()).isNull();
    assertThat(content.getFileType()).isEqualTo("image/jpeg");
  }


  @Test
  void testMapToDto(){
    UserResponseDto resp = userMapper.toDto(user, status, profile);

    assertThat(resp).isNotNull();
    assertThat(resp.profilePictureBase64()).isEqualTo(Base64.getEncoder().encodeToString(profile.getData()));
    assertThat(resp.userId()).isEqualTo(user.getUUID());
    assertThat(resp.userStatus()).isNotNull();
    assertThat(resp.email()).isEqualTo("email@email.com");
    assertThat(resp.phoneNumber()).isEqualTo("01012341234");
    assertThat(resp.username()).isEqualTo("username");
    assertThat(resp.description()).isEqualTo("description");
  }
}
