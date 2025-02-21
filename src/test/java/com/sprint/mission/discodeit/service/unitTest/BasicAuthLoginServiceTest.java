package com.sprint.mission.discodeit.service.unitTest;

//@ExtendWith(MockitoExtension.class)
//class BasicAuthLoginServiceTest {
//
//  @InjectMocks
//  BasicAuthLoginService basicAuthLoginService;
//
//  @Mock
//  AuthLoginValidator authLoginValidator;
//
//  @Mock
//  UserMapper userMapper;
//
//  @Test
//  void login_Success() {
//    // given
//    LoginRequest request = new LoginRequest("이병규", "qwer123!");
//    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);
//    Instant now = Instant.now();
//    UserStatus userStatus = new UserStatus(user.getId(), now);
//    UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(),
//        user.getPhoneNumber(), user.getProfileId(), userStatus.isOnline(), user.getCreatedAt(),
//        user.getUpdatedAt());
//
//    // when
//    when(authLoginValidator.findUserByUsername(request.username())).thenReturn(user);
//    doNothing().when(authLoginValidator).validatePassword(user, request.password());
//    when(authLoginValidator.findUserStatusByUserId(user.getId())).thenReturn(userStatus);
//    when(userMapper.userEntityToDto(user, userStatus.isOnline())).thenReturn(userDto);
//
//    UserDto result = basicAuthLoginService.login(request);
//
//    // then
//    assertNotNull(result);
//    verify(authLoginValidator, times(1)).findUserByUsername(request.username());
//  }
//
//  @Test
//  void login_NotExistsUsername() {
//    // given
//    LoginRequest request = new LoginRequest("이병규", "qwer123!");
//
//    // when
//    when(authLoginValidator.findUserByUsername(request.username()))
//        .thenThrow(ResourceNotFoundException.class);
//
//    // then
//    assertThrows(ResourceNotFoundException.class, () -> basicAuthLoginService.login(request));
//  }
//
//  @Test
//  void login_InvalidPassword() {
//    // given
//    LoginRequest request = new LoginRequest("이병규", "wrongPassword!");
//    User user = new User("이병규", "test@gmail.com", "01011112222", "qwer123!", null);
//
//    // when
//    when(authLoginValidator.findUserByUsername(request.username())).thenReturn(user);
//    doThrow(InvalidResourceException.class).when(authLoginValidator)
//        .validatePassword(user, request.password());
//
//    // then
//    assertThrows(InvalidResourceException.class, () -> basicAuthLoginService.login(request));
//  }
//}