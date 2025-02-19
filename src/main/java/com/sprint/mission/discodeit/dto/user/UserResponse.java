package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private boolean isOnline;

    public UserResponse(User user, boolean isOnline) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.isOnline = isOnline;
    }
}
