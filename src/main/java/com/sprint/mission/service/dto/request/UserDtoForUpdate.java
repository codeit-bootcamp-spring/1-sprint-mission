package com.sprint.mission.service.dto.request;

import com.sprint.mission.entity.BinaryProfileContent;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class UserDtoForUpdate {


    private UUID userId;
    private String username;
    private String password;
    private String email;
    @Setter
    private BinaryProfileContent profileContent;

    public UserDtoForUpdate(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
