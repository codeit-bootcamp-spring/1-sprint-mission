package repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.USER_FILE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileUserRepositoryTest {

  @InjectMocks
  private FileUserRepository repository;

  private static MockedStatic<FileUtil> fileUtilMock;

  private User user1;
  private User user2;
  private User user3;

  private List<User> mockUserList;

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
    System.out.println("[repository] : " + repository.getClass());

    user1 = new User.UserBuilder("user1","pwd1","email@email.com","01012345689")
        .nickname("user1Nickname1").build();

    user2 = new User.UserBuilder("user2","pwd1","email2@email.com","01012345688")
        .nickname("user1Nickname2").build();

    user3 = new User.UserBuilder("user3","pwd1","email3@email.com","01012345687")
        .nickname("user1Nickname3").build();

    mockUserList = new ArrayList<>();
    mockUserList.add(user1);
    mockUserList.add(user2);
  }

  @Test
  void testCreate(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_FILE, User.class))
        .thenReturn(mockUserList);

    User user = repository.create(user3);

    assertThat(user.getUUID()).isEqualTo(user3.getUUID());
    fileUtilMock.verify(() -> FileUtil.saveAllToFile(USER_FILE, mockUserList), times(1));
  }

  @Test
  void testFindById_Success(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_FILE, User.class)).thenReturn(mockUserList);

    Optional<User> user = repository.findById(user1.getUUID());
    assertThat(user).isPresent();

    User actualUser = user.get();
    assertThat(actualUser).isEqualTo(user1);
  }

  @Test
  void testFindById_Fail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_FILE, User.class)).thenReturn(mockUserList);

    Optional<User> user = repository.findById(user3.getUUID());

    assertThat(user).isEmpty();
  }

  @Test
  void testFindAll(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_FILE, User.class)).thenReturn(mockUserList);

    List<User> users = repository.findAll();

    assertThat(users).isNotNull();
    assertThat(users).hasSize(mockUserList.size());
    assertThat(users).contains(user1, user2);
  }

  @Test
  void testUpdate_Success(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_FILE, User.class))
        .thenReturn(mockUserList);

    Optional<User> optionalUser = repository.findById(user1.getUUID());
    assertThat(optionalUser).isPresent();

    User user = optionalUser.get();
    user.setUsername("updatedUsername");

    User updatedUser = repository.update(user);

    assertThat(updatedUser.getUUID()).isEqualTo(user1.getUUID());
    fileUtilMock.verify(() -> FileUtil.saveAllToFile(USER_FILE, mockUserList), times(1));
  }

  @Test
  void testUpdate_Fail(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_FILE, User.class))
        .thenReturn(mockUserList);

    assertThrows(UserNotFoundException.class, () -> repository.update(user3));
    fileUtilMock.verify(() -> FileUtil.saveAllToFile(USER_FILE, mockUserList), times(0));
  }

  @Test
  void testDelete(){
    fileUtilMock.when(() -> FileUtil.loadAllFromFile(USER_FILE, User.class))
        .thenReturn(mockUserList);

    repository.delete(user1.getUUID());

    List<User> users = repository.findAll();
    assertThat(users.size()).isEqualTo(1);
    fileUtilMock.verify(() -> FileUtil.saveAllToFile(USER_FILE, mockUserList), times(1));
  }
}
