package repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.CHANNEL_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileChannelRepositoryTest {

  @InjectMocks
  private FileChannelRepository repository;

  private static MockedStatic<FileUtil> fileUtilMock;

  private Channel channel1;
  private Channel channel2;
  private Channel channel3;
  private List<Channel> mockChannelList;


  @BeforeAll
  static void beforeAll(){
    fileUtilMock = mockStatic(FileUtil.class);
  }

  @AfterAll
  static void afterAll(){
    fileUtilMock.close();
  }

  @BeforeEach
  void setUp() {
    repository = new FileChannelRepository("fileDir/channel.ser");
    channel1 = new Channel.ChannelBuilder("channel1", Channel.ChannelType.VOICE)
        .serverUUID("server1")
        .build();

    channel2 = new Channel.ChannelBuilder("channel2", Channel.ChannelType.CHAT)
        .serverUUID("server1")
        .build();

    channel3 = new Channel.ChannelBuilder("channel3", Channel.ChannelType.VOICE)
        .serverUUID("server2")
        .build();

    mockChannelList = new ArrayList<>();
    mockChannelList.add(channel1);
    mockChannelList.add(channel2);
  }

  @Test
  void testSave(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class))
        .thenReturn(mockChannelList);

    repository.save(channel3);

    fileUtilMock.verify(() -> FileUtil.saveAllToFile(CHANNEL_FILE, mockChannelList), times(1));
  }

  @Test
  void testFindById_Found(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class))
        .thenReturn(mockChannelList);

    Optional<Channel> foundChannel = repository.findById(channel1.getUUID());

    assertTrue(foundChannel.isPresent());
    assertThat(foundChannel.get()).isEqualTo(channel1);
  }

  @Test
  void testFindById_NotFound(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class))
        .thenReturn(mockChannelList);

    Optional<Channel> noChannel = repository.findById("randomId");

    assertThat(noChannel).isEmpty();
  }

  @Test
  void testFindAll(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class))
        .thenReturn(mockChannelList);

    List<Channel> channels = repository.findAll();

    assertThat(channels).hasSize(2);
  }

  @Test
  void testUpdate_Success(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class))
        .thenReturn(new ArrayList<>(mockChannelList));

    Optional<Channel> optionalChannel = repository.findById(channel1.getUUID());
    assertThat(optionalChannel).isPresent();

    Channel existingChannel = optionalChannel.get();
    String uuid = existingChannel.getUUID();
    existingChannel.setChannelName("updatedName");

    Channel result = repository.save(existingChannel);

    fileUtilMock.verify(() -> FileUtil.saveAllToFile(eq(CHANNEL_FILE), argThat((List<Channel> list) ->
        list.stream().anyMatch((Channel ch) -> ch.getUUID().equals(uuid) && ch.getChannelName().equals("updatedName"))
        )), times(1));

    assertThat(result.getUUID()).isEqualTo(uuid);
    assertThat(result.getChannelName()).isEqualTo("updatedName");
  }

  @Test
  void testUpdate_Fail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class))
        .thenReturn(mockChannelList);

    Channel newChannel = new Channel.ChannelBuilder("new", Channel.ChannelType.VOICE).serverUUID("random").build();

    assertThrows(ChannelNotFoundException.class, () ->
        repository.update(newChannel)
        );
  }

  @Test
  void testDelete_Success(){
    List<Channel> initList = new ArrayList<>(mockChannelList);
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class))
        .thenReturn(initList);

    repository.delete(channel1.getUUID());

    fileUtilMock.verify(() -> FileUtil.saveAllToFile(CHANNEL_FILE, initList), times(1));
  }

  @Test
  void testDelete_Fail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class))
        .thenReturn(mockChannelList);

    assertThrows(ChannelNotFoundException.class, () -> repository.delete("random-uuid"));
  }
}
