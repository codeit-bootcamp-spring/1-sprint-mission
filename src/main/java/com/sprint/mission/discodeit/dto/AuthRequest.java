package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // ✅ 기본 생성자
@AllArgsConstructor // ✅ username, password를 받는 생성자 추가
public class AuthRequest {
    private String username;
    private String password;
}
