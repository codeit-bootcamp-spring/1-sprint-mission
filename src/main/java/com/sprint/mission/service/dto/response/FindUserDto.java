package com.sprint.mission.service.dto.response;

import com.sprint.mission.entity.main.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class FindUserDto {

    private String name;
    private String email;
    private Instant createAt;
    private Instant updateAt;

    @Setter
    private boolean isOnline;

    public FindUserDto(User user, Boolean isOnline){
        this.name = user.getName();
        this.email = user.getEmail();
        this.createAt = user.getCreateAt();
        this.updateAt = user.getUpdateAt();
        this.isOnline = isOnline;
    }

    public FindUserDto() {}
}
