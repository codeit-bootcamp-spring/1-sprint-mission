package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private boolean online = false;
    private String profileImage;
}