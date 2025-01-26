package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // FileUserService 인스턴스 생성
        UserService userService = new FileUserService();

        try {
            System.out.println("=== 사용자 추가 테스트 ===");
            // 사용자 추가
            UUID user1Id = UUID.randomUUID();
            User user1 = new User(user1Id, "JohnDoe", "password123");
            userService.addUser(user1);

            UUID user2Id = UUID.randomUUID();
            User user2 = new User(user2Id, "JaneDoe", "password456");
            userService.addUser(user2);

            System.out.println("Added Users:");
            System.out.println(userService.getUser(user1Id));
            System.out.println(userService.getUser(user2Id));

            System.out.println("\n=== 모든 사용자 조회 ===");
            // 모든 사용자 조회
            List<User> allUsers = userService.getAllUsers();
            allUsers.forEach(System.out::println);

            System.out.println("\n=== 사용자 업데이트 테스트 ===");
            // 사용자 업데이트
            userService.updateUser(user1Id, "JohnDoeUpdated", "newpassword123");
            System.out.println("Updated User: " + userService.getUser(user1Id));

            System.out.println("\n=== 사용자 삭제 테스트 ===");
            // 사용자 삭제
            userService.deleteUser(user2Id);
            System.out.println("Deleted User with ID: " + user2Id);

            System.out.println("\n=== 삭제 후 모든 사용자 조회 ===");
            // 모든 사용자 조회 (삭제 후)
            allUsers = userService.getAllUsers();
            allUsers.forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
