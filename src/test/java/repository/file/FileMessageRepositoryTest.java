package repository.file;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.sprint.mission.discodeit.constant.FileConstant.MESSAGE_FILE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class FileMessageRepositoryTest {

  @InjectMocks
  private FileMessageRepository repository;

  private static MockedStatic<FileUtil> fileUtilMock;
  private Message message1;
  private Message message2;

  private Message message3;

  private List<Message> mockMessageList;

  @BeforeAll
  static void beforeAll(){
    fileUtilMock = mockStatic(FileUtil.class);
  }

  @AfterAll
  static void afterAll(){
    fileUtilMock.close();
  }

  @BeforeEach
  void setUp() throws InterruptedException {

    repository = new FileMessageRepository("fileDir/message.ser");
    repository.clear();
    message1 = new Message.MessageBuilder(
        "userUUID",
        "channelUUID",
        "this is content"
    ).build();
    TimeUnit.MILLISECONDS.sleep(50);
    message2 = new Message.MessageBuilder(
        "userUUID",
        "channelUUID",
        "this is content2"
    ).build();

    message3 = new Message.MessageBuilder(
        "userUUID",
        "channelUUID2",
        "this is content3"
    ).build();

    mockMessageList = new ArrayList<>();
    mockMessageList.add(message1);
    mockMessageList.add(message2);
    mockMessageList.add(message3);
  }

  @Test
  void testCreate(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(mockMessageList);

    repository.create(message3);

    fileUtilMock.verify(() -> FileUtil.saveAllToFile(MESSAGE_FILE, mockMessageList),times(1));
  }

  @Test
  void testFindById_Found(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(mockMessageList);

    Optional<Message> message = repository.findById(message1.getUUID());

    assertThat(message).isPresent();
    assertThat(message.get().getUUID()).isEqualTo(message1.getUUID());
  }

  @Test
  void testFindById_NotFound(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(mockMessageList);

    Optional<Message> message = repository.findById("randomId");

    assertThat(message).isEmpty();
  }

  @Test
  void findByChannel_Found(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(mockMessageList);

    List<Message> messages = repository.findByChannel("channelUUID");

    assertThat(messages).hasSize(2);
  }

  @Test
  void findByChannel_NotFound(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(mockMessageList);

    List<Message> messages = repository.findByChannel("random");

    assertThat(messages).hasSize(0);
  }

  @Test
  void testFindAll(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(mockMessageList);

    List<Message> messages = repository.findAll();

    assertThat(messages).hasSize(3);
  }

  @Test
  void testFindLatestMessage(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(mockMessageList);

    Message message = repository.findLatestChannelMessage("channelUUID");

    assertThat(message.getUUID()).isEqualTo(message2.getUUID());
  }

  @Test
  void testUpdate_Success(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(new ArrayList<>(mockMessageList));

    Optional<Message> originalMessage = repository.findById(message1.getUUID());
    assertThat(originalMessage).isPresent();


    Message actualMessage = originalMessage.get();
    actualMessage.setContent("new content");

    Message updatedMessage = repository.update(actualMessage);

    assertThat(updatedMessage.getUUID()).isEqualTo(message1.getUUID());
    assertThat(updatedMessage.getContent()).isEqualTo("new content");
  }

  @Test
  void testUpdate_Fail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(mockMessageList);

    Message message = new Message.MessageBuilder("test", "test", "test")
        .build();

    assertThrows(MessageNotFoundException.class, () ->
        repository.update(message)
        );
  }

  @Test
  void testDeleteByChannel(){

    fileUtilMock.when(() -> FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class))
        .thenReturn(mockMessageList);

    repository.delete(message1.getUUID());

    fileUtilMock.verify(() -> FileUtil.saveAllToFile(MESSAGE_FILE, mockMessageList), times(1));
  }
}
