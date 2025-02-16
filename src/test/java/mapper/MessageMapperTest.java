package mapper;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = DiscodeitApplication.class)
public class MessageMapperTest {

  @Autowired
  private MessageMapper messageMapper;

  private CreateMessageDto createMessageDto;
  private Message message;
  private List<BinaryContent> binaryContents;

  @BeforeEach
  void setUp() throws IOException {
    // 1️⃣ 테스트용 MultipartFile 생성
    MockMultipartFile file = new MockMultipartFile(
        "file", "test.jpg", "image/jpeg", "test image content".getBytes()
    );

    // 2️⃣ CreateMessageDto 생성
    createMessageDto = new CreateMessageDto();
    createMessageDto.setUserId("user123");
    createMessageDto.setChannelId("channel123");
    createMessageDto.setContent("This is a test message");
    createMessageDto.setMultipart(List.of(file));

    // 3️⃣ Message 엔티티 생성
    message = new Message("user123", "channel123", "This is a test message", List.of());

    // 4️⃣ BinaryContent 생성
    binaryContents = List.of(
        new BinaryContent(
            "user123",
            message.getUUID(),
            "channel123",
            "test.jpg",
            "image/jpeg",
            file.getSize(),
            file.getBytes(),
            false
        )
    );

    message.setBinaryContents(binaryContents);
  }

  @Test
  void testToEntity(){
    Message m = messageMapper.toEntity(createMessageDto);

    assertThat(m).isNotNull();
    assertThat(m.getUUID()).isNotNull();
    assertThat(m.getUserId()).isEqualTo(createMessageDto.getUserId());
    assertThat(m.getChannelId()).isEqualTo(createMessageDto.getChannelId());
    assertThat(m.getContent()).isEqualTo(createMessageDto.getContent());
    assertThat(m.getBinaryContents()).isNull();
  }



  @Test
  void testToDto(){
    try (MockedStatic<BinaryContentUtil> mockedStatic = Mockito.mockStatic(BinaryContentUtil.class)) {
      mockedStatic.when(() -> BinaryContentUtil.convertMultipleBinaryContentToBase64(binaryContents))
          .thenReturn(List.of(Base64.getEncoder().encodeToString(binaryContents.get(0).getData())));


      MessageResponseDto dto = messageMapper.toResponseDto(message);

      assertThat(dto).isNotNull();
      assertThat(dto.messageId()).isEqualTo(message.getUUID());
      assertThat(dto.userId()).isEqualTo(message.getUserId());
      assertThat(dto.channelId()).isEqualTo(message.getChannelId());
      assertThat(dto.createdAt()).isNotNull();
      assertThat(dto.base64Data()).isNotEmpty();
    }
  }

}
