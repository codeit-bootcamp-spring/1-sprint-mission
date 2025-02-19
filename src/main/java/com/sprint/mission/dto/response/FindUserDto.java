package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class FindUserDto {

    private String name;
    private String email;
    private Instant createAt;
    private Instant updateAt;
    private byte[] profile;

    @Setter
    private boolean isOnline;

    // 나중에 USER가 ONLINE 상태 가지도록하면 수정
    public FindUserDto(User user, byte[] profile, Boolean isOnline){
        this.name = user.getName();
        this.email = user.getEmail();
        this.createAt = user.getCreateAt();
        this.updateAt = user.getUpdateAt();
        this.isOnline = isOnline;
        this.profile = profile;
    }

    // 유저별 lastReadTime 관련 map용 dto (profile null)
    public FindUserDto(User user, Boolean isOnline){
        this.name = user.getName();
        this.email = user.getEmail();
        this.createAt = user.getCreateAt();
        this.updateAt = user.getUpdateAt();
        this.isOnline = isOnline;
    }

    public FindUserDto() {}
}
