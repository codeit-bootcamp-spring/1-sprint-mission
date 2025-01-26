//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class JCFUserServiceTest {
//  private JCFUserService userService;
//
//  @BeforeEach
//  void setUp() {
//    UserRepository userRepository = new JCFUserRepository();
//    userService = new JCFUserService(userRepository);
//  }
//
//  @DisplayName("사용자 생성 테스트")
//  @Test
//  void testCreateUser() {
//    // when
//    userService.createUser("1111", "Alice");
//    userService.createUser("2222", "Bob");
//
//    // then
//    assertEquals(2, userService.findAllUsers().size());
//    assertEquals("Alice", userService.findUserByName("Alice").getName());
//  }
//
//  @DisplayName("사용자 이름 업데이트 테스트")
//  @Test
//  void testUpdateUserName() {
//    // given
//    userService.createUser("3333", "Charlie");
//
//    // when
//    String newName = "newCharlie";
//    userService.updateUserName(userService.findUserByName("Charlie").getId(), newName);
//
//    // then
//    assertEquals(newName, userService.findUserByName(newName).getName());
//  }
//
//  @DisplayName("사용자 삭제 테스트")
//  @Test
//  void testRemoveUser() {
//    // given
//    userService.createUser("5555", "Eve");
//
//    // when
//    userService.removeUser(userService.findUserByName("Eve").getId());
//
//    // then
//    assertEquals(0, userService.findAllUsers().size());
//  }
//
//}