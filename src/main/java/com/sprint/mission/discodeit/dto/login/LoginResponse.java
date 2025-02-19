package com.sprint.mission.discodeit.dto.login;

import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UUID id;
    private String username;
    private String email;

    public LoginResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
