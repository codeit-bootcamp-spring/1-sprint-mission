package com.sprint.mission.service.dto.request;

import com.sprint.mission.entity.BinaryProfileContent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDtoForRequest {

    private final String username;
    private final String password;
    private final String email;
    // 프로필 이미지
    private BinaryProfileContent profileImg;

    public UserDtoForRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
