package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.dto.authDto.LoginDto;
import com.sprint.mission.discodeit.service.AuthService;

public class AuthTest {
    public static void loginTest(AuthService authService) {
        String name = "test_name1";
        String password = "PWpw1!!";

        LoginDto loginDto = new LoginDto(name, password);

        // 로그인 성공 시 메시지 (실패 시에는 예외 뜸)
        System.out.println("성공적으로 로그인되었습니다.");
    }
}
