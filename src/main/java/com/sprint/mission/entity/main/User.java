package com.sprint.mission.entity.main;


import com.sprint.mission.dto.request.UserDtoForCreate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createAt;
    private Instant updateAt;

    private String name;
    private String email;
    private String password;

    private UUID profileImgId;

    public User(String name, String password, String email, UUID profileImgId) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.name = name;
        this.password = password;
        this.email = email;
        this.profileImgId = profileImgId;
    }
}
