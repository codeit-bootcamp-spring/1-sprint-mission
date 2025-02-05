//package UserServiceTest;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//import com.sprint.mission.discodeit.dto.user.CreateUserDto;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.UserStatusRepository;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import static org.mockito.Mockito.when;
//
//public class BasicUserServiceTest {
//
//  private UserRepository userRepository;
//  private UserStatusRepository userStatusRepository;
//  private BinaryContentRepository binaryContentRepository;
//  private BasicUserService basicUserService;
//
//  @BeforeEach
//  void setup(){
//    userRepository = Mockito.mock(UserRepository.class);
//    userStatusRepository = Mockito.mock(UserStatusRepository.class);
//    binaryContentRepository = Mockito.mock(BinaryContentRepository.class);
//    basicUserService = new BasicUserService(userRepository, userStatusRepository, binaryContentRepository);
//  }
//
//  @Test
//  void testCreateUser(){
//    CreateUserDto dto = new CreateUserDto(
//        "testUser",
//        "testPwd",
//        "test@gmail.com",
//        "testNickname",
//        "01012345678",
//        null,
//        null,
//        null,
//        "test description"
//    );
//
//    User mockUser = new User.UserBuilder(dto.username(), dto.password(), dto.email(),dto.phoneNumber()).nickname(dto.nickname()).description(dto.description()).build();
//
//    when(userRepository.create(any(User.class))).thenReturn(mockUser);
//
//    User createdUser = basicUserService.createUser(dto);
//    assertNotNull(createdUser);
//    assertEquals(dto.email(), createdUser.getEmail());
//  }
//}
