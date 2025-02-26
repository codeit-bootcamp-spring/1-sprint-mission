//package com.sprint.mission.discodeit.repository.file.json;
//
//import com.sprint.mission.discodeit.entity.status.user.UserStatus;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.UserStatusRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.time.Duration;
//import java.time.Instant;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class JsonUserStatusRepositoryTest {
//
//    UserRepository userRepository = new JsonUserRepository();
//    UserStatusRepository userStatusRepository = new JsonUserStatusRepository();
//
//    @Test
//    @DisplayName("레포지토리 저장")
//    void save() {
//        User user = new User("userA", "test@email.com", "password");
//        userRepository.userSave(user);
//
//        UserStatus status = new UserStatus(user.getId());
//
//        userStatusRepository.save(status);
//
//        UserStatus findStatus = userStatusRepository.findById(user.getId());
//        assertThat(findStatus).isEqualTo(status);
//    }
//
//    @Test
//    @DisplayName("유저 상태 업데이트")
//    void update() throws InterruptedException {
//        User user = new User("userA", "test@email.com", "password");
//        User savedUser = userRepository.userSave(user);
//        UserStatus status = new UserStatus(user.getId());
//
//        Instant before = status.getLastAccessTime();
//        System.out.println("before = " + before);
//        Thread.sleep(2000); // 2초 대기
//
//        // 유저 상태 업데이트
//        status.updateUserStatus();
//
//        UserStatus savedUserState = userStatusRepository.save(status);
//        UserStatus findUserState = userStatusRepository.findById(user.getId());
//
//        Instant after = findUserState.getLastAccessTime();
//        userStatusRepository.updateState(savedUser.getId(), savedUserState);
//
//        System.out.println("after = " + after);
//
//        Duration duration = Duration.between(before, after);
//
//        assertThat(duration.getSeconds()).isGreaterThanOrEqualTo(2L);
//    }
//
//    @Test
//    @DisplayName("userStatus 삭제")
//    void delete() {
//        User user = new User("userA", "test@email.com", "password");
//        User user2 = new User("userB", "testB@email.com", "BBpassword");
//        userRepository.userSave(user);
//        userRepository.userSave(user2);
//
//        UserStatus status = new UserStatus(user.getId());
//        UserStatus status2 = new UserStatus(user2.getId());
//        userStatusRepository.save(status);
//        userStatusRepository.save(status2);
//
//        UserStatus findUser = userStatusRepository.findById(user.getId());
//
//        userStatusRepository.remove(user.getId());
//
//
//        UserStatus byId = userStatusRepository.findById(user.getId());
//        assertThat(byId).isNull();
//    }
//}