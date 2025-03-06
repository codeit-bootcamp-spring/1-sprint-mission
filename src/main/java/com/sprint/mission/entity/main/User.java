package com.sprint.mission.entity.main;


import com.sprint.mission.config.BaseTimeEntity;
import com.sprint.mission.dto.request.UserDtoForCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@EqualsAndHashCode(callSuper = false)
@ToString @Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "유저")
public class User extends BaseTimeEntity implements Serializable {

    @ToString.Exclude
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String email;
    private String password;

    private UUID profileImgId;

    public User(String name, String password, String email, UUID profileImgId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.password = password;
        this.email = email;
        this.profileImgId = profileImgId;
    }

    public User(String name, String password, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.password = password;
        this.email = email;
    }

}
