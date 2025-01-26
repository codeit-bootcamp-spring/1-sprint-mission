package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.UUID;

public class JavaApplication {

    static void userCRUDTest(UserService userService) {
        System.out.println("=== 사용자 추가 테스트 ===");
        User user1 = new User(UUID.randomUUID(), "JohnDoe", "password123");
        User user2 = new User(UUID.randomUUID(), "JaneDoe", "password456");
        userService.addUser(user1);
        userService.addUser(user2);

        System.out.println("Added Users:");
        userService.getAllUsers().forEach(System.out::println);

        System.out.println("\n=== 모든 사용자 조회 ===");
        userService.getAllUsers().forEach(System.out::println);

        System.out.println("\n=== 사용자 업데이트 테스트 ===");
        userService.updateUser(user1.getId(), "JohnDoeUpdated", "newpassword123");
        System.out.println("Updated User: " + userService.getUser(user1.getId()));

        System.out.println("\n=== 사용자 삭제 테스트 ===");
        userService.deleteUser(user2.getId());
        System.out.println("Deleted User with ID: " + user2.getId());

        System.out.println("\n=== 삭제 후 모든 사용자 조회 ===");
        userService.getAllUsers().forEach(System.out::println);
    }

    public static void main(String[] args) {
        // JCF 기반 저장소 테스트
        System.out.println("=== JCF 기반 테스트 ===");
        UserService jcfUserService = new BasicUserService(new JCFUserRepository());
        userCRUDTest(jcfUserService);

        // File 기반 저장소 테스트
        System.out.println("\n=== File 기반 테스트 ===");
        UserService fileUserService = new BasicUserService(new FileUserRepository());
        userCRUDTest(fileUserService);
    }
}
