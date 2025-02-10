package repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.USER_STATUS_FILE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileUserStatusRepositoryTest {

  @InjectMocks
  private FileUserStatusRepository repository;

  private static MockedStatic<FileUtil> fileUtilMock;

  private UserStatus userStatus1;
  private UserStatus userStatus2;
  private UserStatus userStatus3;
  private List<UserStatus> mockUserStatusList;
  @BeforeAll
  static void beforeAll(){

  }
  @AfterAll
  static void afterAll(){
//    fileUtilMock.close();
  }

  @BeforeEach
  void setUp(){
    fileUtilMock = mockStatic(FileUtil.class);
    repository = new FileUserStatusRepository("fileDir/user_status.ser");
    userStatus1 = new UserStatus("user1", Instant.now());
    userStatus2 = new UserStatus("user2", Instant.now());
    userStatus3 = new UserStatus("user3", Instant.now());
    mockUserStatusList = new ArrayList<>();
    mockUserStatusList.add(userStatus1);
    mockUserStatusList.add(userStatus2);
  }
  @AfterEach
  void afterEach(){
    fileUtilMock.close();;
  }
  @Test
  void testSave_New(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class)).thenReturn(mockUserStatusList);

    UserStatus newUserStatus = repository.save(userStatus3);

    assertThat(newUserStatus.getUUID()).isEqualTo(userStatus3.getUUID());
    fileUtilMock.verify(() -> FileUtil.saveAllToFile(USER_STATUS_FILE, mockUserStatusList), times(1));
  }

  @Test
  void testFindById_Success(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class)).thenReturn(mockUserStatusList);

    Optional<UserStatus> optionalStatus = repository.findById(userStatus1.getUUID());
    assertThat(optionalStatus).isPresent();

    UserStatus status = optionalStatus.get();
    assertThat(status).isEqualTo(userStatus1);
  }

  @Test
  void testFindById_Fail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class)).thenReturn(mockUserStatusList);

    Optional<UserStatus> optionalUserStatus = repository.findById("randomId");
    assertThat(optionalUserStatus).isEmpty();
  }

  @Test
  void testSave_Update(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class)).thenReturn(mockUserStatusList);

    Optional<UserStatus> optionalStatus = repository.findById(userStatus1.getUUID());
    assertThat(optionalStatus).isPresent();

    UserStatus status = optionalStatus.get();
    status.updateLastOnline();

    repository.save(status);

    fileUtilMock.verify(() -> FileUtil.saveAllToFile(USER_STATUS_FILE, mockUserStatusList), times(1));
  }

  @Test
  void testFindByUserId_SuccessAndFail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class)).thenReturn(mockUserStatusList);

    Optional<UserStatus> optionalExist = repository.findByUserId(userStatus1.getUserId());
    Optional<UserStatus> optionalNotExist = repository.findByUserId("randomId");

    assertThat(optionalExist).isPresent();
    assertThat(optionalNotExist).isEmpty();
  }

  @Test
  void testFindAll(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class)).thenReturn(mockUserStatusList);

    List<UserStatus> statuses = repository.findAll();
    assertThat(statuses).hasSize(2);
    assertThat(statuses).contains(userStatus1, userStatus2);
  }

  @Test
  void testDeleteByUserId(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class)).thenReturn(mockUserStatusList);

    repository.deleteByUserId(userStatus1.getUserId());

    fileUtilMock.verify(() ->
          FileUtil.saveAllToFile(eq(USER_STATUS_FILE), argThat((List<UserStatus> list) ->
                list.stream().noneMatch(status -> status.getUUID().equals(userStatus1.getUUID()))
              )), times(1)
        );
  }

  @Test
  void testDeleteById(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_STATUS_FILE, UserStatus.class)).thenReturn(mockUserStatusList);

    repository.deleteById(userStatus1.getUUID());

    fileUtilMock.verify(() ->
          FileUtil.saveAllToFile(eq(USER_STATUS_FILE), argThat((List<UserStatus> list) ->
                list.stream().noneMatch(status -> status.getUUID().equals(userStatus1.getUUID()))
              )), times(1)
        );
  }
}
