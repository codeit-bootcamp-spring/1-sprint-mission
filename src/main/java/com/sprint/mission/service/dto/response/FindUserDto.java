package com.sprint.mission.service.dto.response;

import com.sprint.mission.entity.main.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class FindUserDto {

    private final String name;
    private final String email;
    private final Instant createAt;
    private final Instant updateAt;

    @Setter
    private boolean isOnline;

    public FindUserDto(User user, Boolean isOnline){
        this.name = user.getName();
        this.email = user.getEmail();
        this.createAt = user.getCreateAt();
        this.updateAt = user.getUpdateAt();
        this.isOnline = isOnline;
    }
}
