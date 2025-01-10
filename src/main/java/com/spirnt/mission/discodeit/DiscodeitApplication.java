package com.spirnt.mission.discodeit;

import com.spirnt.mission.discodeit.entity.*;
import com.spirnt.mission.discodeit.jcf.*;
import com.spirnt.mission.discodeit.service.*;

import java.util.UUID;

public class DiscodeitApplication {
    public static void main(String[] args) {
        // 1. UserService 초기화
        UserService userService = new JCFUserService();

        // 2. 사용자 등록
        User user1 = new User("Martin", "user1@example.com");
        User user2 = new User("Ayden", "user2@example.com");

        userService.create(user1);
        userService.create(user2);
        System.out.println("사용자 등록 완료.");

        // 3. 사용자 조회 - 다건
        System.out.println("모든 사용자 조회:");
        userService.readAll().forEach(user -> {
            System.out.println("username : " + user.getUsername() + " | Email : " + user.getEmail());
        });

        // 4. 사용자 조회 - 단건
        UUID user1Id = user1.getId();
        System.out.println("단일 사용자 조회(user1):");
        System.out.println(userService.read(user1Id).getUsername());

        // 5. 사용자 수정
        System.out.println("사용자 수정:");
        userService.update(user1Id, new User("updated_user1@example.com", "Updated@ User One"));

        // 6. 수정된 사용자 조회
        System.out.println("수정된 사용자 조회:");
        User modifyUser = userService.read(user1Id);
        System.out.println(modifyUser.getUsername() + " | " + modifyUser.getEmail());

        // 7. 사용자 삭제
        System.out.println("사용자 삭제:");
        userService.delete(user1Id);
        userService.delete(user1Id);

        // 8. 삭제 후 확인
        System.out.println("삭제된 사용자 확인:");
        System.out.println(userService.read(user1Id)); // null 또는 삭제 확인 메시지 출력
    }
}
