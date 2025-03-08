package com.sprint.mission.entity.main;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@EqualsAndHashCode(callSuper = false)
@ToString @Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "유저")
public class User extends BaseUpdatableEntity{

    private String name;
    private String email;
    private String password;

    private UUID profileImgId;

    public User(String name, String password, String email, UUID profileImgId) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.profileImgId = profileImgId;
    }

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

}
