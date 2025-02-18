package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.dto.userDto.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.userDto.UpdateUserRequestDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.view.DisplayUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class UserTest {

    // 유저 생성 시 자동으로 이메일, 이름, 닉네임을 다르게 하기 위한 필드
    private static int userIndex = 1;

    // 유저 생성
    public static UUID setUpUser(UserService userService) throws IOException {

        String email = "test" + userIndex + "@email.com";
        String password = "PWpw11!!";
        String name = "test_name" + userIndex;
        String nickname = "test_nickname" + (userIndex++);
        String phoneNumber = "010-0000-0000";
        MultipartFile profileImage = null;

        CreateUserRequestDto user = new CreateUserRequestDto(email, password, name, nickname, phoneNumber, profileImage);

        UUID id = userService.create(user);

        System.out.println("================================================================================");
        System.out.println("유저 생성 결과 : ");
        DisplayUser.displayUserInfo(user);
        System.out.println("================================================================================");

        return id;
    }

    // 유저 정보 변경
    public static void updateUser(UUID id, UserService userService) throws IOException {

        String email = "update@email.com";
        String password = null;
        String name = "updateName";
        String nickname = "update_nickname";
        String phoneNumber = "010-1111-0000";
        MultipartFile profileImageFile = null;

        UpdateUserRequestDto updateUser = new UpdateUserRequestDto(email, password, name, nickname, phoneNumber, profileImageFile);

        userService.updateUser(id, updateUser);
    }

    // 유저 삭제
    public static void deleteUser(UUID id, UserService userService) {

        System.out.println("================================================================================");
        System.out.println("유저 삭제 전 목록 :");
        DisplayUser.displayAllUserInfo(userService.findAll());
        System.out.println("================================================================================");

        userService.delete(id);

        System.out.println("================================================================================");
        System.out.println("유저 삭제 후 목록 :");
        DisplayUser.displayAllUserInfo(userService.findAll());
        System.out.println("================================================================================");
    }
}
