package repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.file.FileReadStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.READ_STATUS_FILE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileReadStatusRepositoryTest {

  @InjectMocks
  private FileReadStatusRepository repository;

  private static MockedStatic<FileUtil> fileUtilMock;

  private ReadStatus status1;
  private ReadStatus status2;
  private ReadStatus status3;
  private ReadStatus status4;

  private List<ReadStatus> mockStatusList;
  @BeforeAll
  static void beforeAll(){
    fileUtilMock = mockStatic(FileUtil.class);
  }

  @AfterAll
  static void afterAll(){
    fileUtilMock.close();
  }

  @BeforeEach
  void setUp(){

    repository = new FileReadStatusRepository("fileDir/read_status.ser");

    status1 = new ReadStatus("channel1", "user1");
    status2 = new ReadStatus("channel1", "user2");
    status3 = new ReadStatus("channel2", "user1");
    status4 = new ReadStatus("channel1", "user3");

    mockStatusList = new ArrayList<>();
    mockStatusList.add(status1);
    mockStatusList.add(status2);
    mockStatusList.add(status3);
  }

  @Test
  void testSave_New(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    ReadStatus status = repository.save(status4);

    fileUtilMock.verify(() -> FileUtil.saveAllToFile(READ_STATUS_FILE, mockStatusList), times(1));
  }

  @Test
  void testSave_Update(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    Optional<ReadStatus> optionalStatus = repository.findById(status1.getUUID());
    assertThat(optionalStatus).isPresent();

    Instant beforeLastReadAt = status1.getLastReadAt();

    ReadStatus originalStatus = optionalStatus.get();
    originalStatus.updateLastReadAt();

    ReadStatus afterStatus = repository.save(originalStatus);

    assertThat(afterStatus.getLastReadAt()).isNotEqualTo(beforeLastReadAt);
    fileUtilMock.verify(() -> FileUtil.saveAllToFile(READ_STATUS_FILE, mockStatusList), times(1));
  }


  @Test
  void findById_Success(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    Optional<ReadStatus> status = repository.findById(status1.getUUID());
    assertThat(status).isPresent();

    ReadStatus actualStatus = status.get();

    assertThat(actualStatus.getUUID()).isEqualTo(status1.getUUID());
  }

  @Test
  void findById_Fail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    Optional<ReadStatus> status = repository.findById("random");

    assertThat(status).isEmpty();
  }

  @Test
  void testFindByUserId_Success(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    List<ReadStatus> statuses = repository.findByUserId("user1");

    assertThat(statuses).hasSize(2);
  }

  @Test
  void testFindByUserId_Fail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    List<ReadStatus> statuses = repository.findByUserId("random");

    assertThat(statuses).isEmpty();
  }

  @Test
  void testFindByChannelId_Success(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    List<ReadStatus> statuses = repository.findByChannelId("channel1");

    assertThat(statuses).hasSize(2);
  }

  @Test
  void testFindByChannelId_Fail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    List<ReadStatus> statuses = repository.findByChannelId("random");

    assertThat(statuses).hasSize(0);
  }

  @Test
  void testFindByChannelIdAndUserId_Success(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    Optional<ReadStatus> status = repository.findByChannelIdAndUserId(status1.getChannelId(), status1.getUserId());
    assertThat(status).isPresent();
    ReadStatus actualStatus= status.get();

    assertThat(actualStatus).isEqualTo(status1);
  }

  @Test
  void testFindByChannelIdAndUserId_FailByUserId(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    Optional<ReadStatus> status = repository.findByChannelIdAndUserId(status1.getChannelId(), "random");
    assertThat(status).isEmpty();
  }

  @Test
  void testFindByChannelIdAndUserId_FailByChannelId(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    Optional<ReadStatus> status = repository.findByChannelIdAndUserId("random", status1.getUserId());
    assertThat(status).isEmpty();
  }

  @Test
  void testFindByChannelIdAndUserId_FailByBoth(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    Optional<ReadStatus> status = repository.findByChannelIdAndUserId("random", "random");
    assertThat(status).isEmpty();
  }

  @Test
  void testFindAll(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    List<ReadStatus> statuses = repository.findAll();

    assertThat(statuses).hasSize(3);
  }

  @Test
  void testDeleteByUserId(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    repository.deleteByUserId(status1.getUserId());

    List<ReadStatus> statuses = repository.findAll();

    assertThat(statuses).hasSize(1);
    fileUtilMock.verify(() -> FileUtil.saveAllToFile(READ_STATUS_FILE, mockStatusList), times(1));
  }


  @Test
  void testDeleteByChannelId(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(READ_STATUS_FILE, ReadStatus.class)).thenReturn(mockStatusList);

    List<ReadStatus> statuses1 = repository.findAll();



    repository.deleteByChannelId("channel1");

    List<ReadStatus> statuses = repository.findAll();

    assertThat(statuses).hasSize(1);
    fileUtilMock.verify(() -> FileUtil.saveAllToFile(eq(READ_STATUS_FILE), argThat((List<ReadStatus> readStatus) ->
        readStatus.stream().noneMatch(status -> status.getChannelId().equals("channel1"))
    )), times(1));
  }
}
