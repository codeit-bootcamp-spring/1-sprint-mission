package com.sprint.mission.discodeit.entity.user;

import com.sprint.mission.discodeit.entity.BaseEntity;
import lombok.Getter;

import java.io.Serial;
import java.util.UUID;

@Getter
public class User extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 13L;
    private String name;
    private String email;
    private String password;

    private UUID profileId;

    public User(String name, String email, String password, UUID profileId) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public void update(String name, String email, String password, UUID profileId) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (password != null) this.password = password;
        this.profileId = profileId; // 프로필 이미지 선택
        updateTimeStamp();
    }
}

