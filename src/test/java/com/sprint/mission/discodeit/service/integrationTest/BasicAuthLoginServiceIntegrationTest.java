package com.sprint.mission.discodeit.service.integrationTest;

//@SpringBootTest
//@Transactional
//class BasicAuthLoginServiceIntegrationTest {
//
//  @Autowired
//  private BasicAuthLoginService basicAuthLoginService;
//
//  @Autowired
//  private AuthLoginValidator authLoginValidator;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Autowired
//  private UserStatusRepository userStatusRepository;
//
//  @Test
//  void login_Success() {
//    // given
//    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);
//    userRepository.save(user);
//
//    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
//    userStatusRepository.save(userStatus);
//
//    LoginRequest request = new LoginRequest("이병규", "qwer123!");
//
//    // when
//    UserDto result = basicAuthLoginService.login(request);
//
//    // then
//    assertNotNull(result);
//    assertEquals("이병규", result.username());
//    assertEquals("test@gmail.com", result.email());
//  }
//
//  @Test
//  void login_NotExistsUsername() {
//    // given
//    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);
//    userRepository.save(user);
//
//    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
//    userStatusRepository.save(userStatus);
//
//    LoginRequest request = new LoginRequest("코드잇", "qwer123!");
//
//    // when, then
//    assertThrows(ResourceNotFoundException.class, () -> basicAuthLoginService.login(request));
//  }
//
//  @Test
//  void login_InvalidPassword() {
//    // given
//    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);
//    userRepository.save(user);
//
//    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
//    userStatusRepository.save(userStatus);
//
//    LoginRequest request = new LoginRequest("이병규", "asdf909!");
//
//    // when, then
//    assertThrows(InvalidResourceException.class, () -> basicAuthLoginService.login(request));
//  }
//}
