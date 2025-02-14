package integration;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class BinaryContentServiceImplIntegrationTest {
  @Autowired
  private BinaryContentService binaryContentService;

  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;
  private UserResponseDto user;

  @BeforeEach
  void setUp() {

    userRepository.clear();
    binaryContentRepository.clear();
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());


    user = userService.saveUser(
        new CreateUserRequest(
            "username",
            "pwd",
            "test@example.com",
            "testNickname",
            "01012341234",
            mockFile,
            "description"
        )
    );
  }


  @Test
  void BinaryContent_생성_성공(){
    CreateBinaryContentDto dto = new CreateBinaryContentDto(
        user.userId(),
        null,
        "fileName",
        "FileType.JPG",
        3,
        new byte[]{1,1,1},
        false
    );

    BinaryContent binaryContent = binaryContentService.create(dto);

    assertThat(binaryContent).isNotNull();
    assertThat(binaryContent.getData()).hasSize(3);
    assertThat(binaryContent.getMessageId()).isNull();
    assertThat(binaryContent.getUserId()).isEqualTo(user.userId());
  }

  @Test
  void 존재하지_않는_사용자_BinaryContent_생성_실패(){
    CreateBinaryContentDto dto = new CreateBinaryContentDto(
        "invalid-id",
        null,
        "fileName",
        "FileType.JPG",
        3,
        new byte[]{1,1,1}
    );

    assertThatThrownBy(() -> binaryContentService.create(dto)).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void BinaryContent_조회_성공(){
    CreateBinaryContentDto dto = new CreateBinaryContentDto(
        user.userId(),
        null,
        "fileName1",
        "FileType.JPG",
        3,
        new byte[]{1,1,1}
    );

    BinaryContent content = binaryContentService.create(dto);

    BinaryContent findWithId = binaryContentService.find(content.getUUID());

    assertThat(findWithId).isNotNull();
    assertThat(findWithId.getUUID()).isEqualTo(content.getUUID());

  }

  @Test
  void 여러_BinaryContent_ID로_조회_성공(){
    CreateBinaryContentDto dto = new CreateBinaryContentDto(
        user.userId(),
        null,
        "fileName",
        "FileType.JPG",
        3,
        new byte[]{1,1,1}
    );
    CreateBinaryContentDto dto2 = new CreateBinaryContentDto(
        user.userId(),
        null,
        "fileName2",
        "FileType.JPG",
        3,
        new byte[]{1,1,1}
    );

    BinaryContent content1= binaryContentService.create(dto);
    BinaryContent content2 = binaryContentService.create(dto2);

    List<BinaryContent> contents = binaryContentService.findAllByIdIn(List.of(content1.getUUID(), content2.getUUID()));

    assertThat(contents).isNotEmpty();
    assertThat(contents).hasSize(2);
    assertThat(contents).extracting("userId").containsOnly(user.userId());
    assertThat(contents).extracting("fileName").contains("fileName", "fileName2");
  }

  @Test
  void 삭제_성공(){
    CreateBinaryContentDto dto = new CreateBinaryContentDto(
        user.userId(),
        null,
        "fileName",
        "FileType.JPG",
        3,
        new byte[]{1,1,1}
    );
    BinaryContent content1= binaryContentService.create(dto);

    assertThat(binaryContentService.find(content1.getUUID())).isNotNull();

    binaryContentService.delete(content1.getUUID());

    assertThatThrownBy(() -> binaryContentService.find(content1.getUUID())).isInstanceOf(InvalidOperationException.class);
  }
}
