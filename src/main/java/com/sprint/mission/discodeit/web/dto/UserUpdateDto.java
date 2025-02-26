package com.sprint.mission.discodeit.web.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.Instant;

@Getter
public class UserUpdateDto{

    @NotEmpty
    private String userName;
    @NotEmpty
    private String userEmail;
    @NotEmpty
    private transient String password;
    @NotEmpty
    private String loginId;
    private Instant updateAt;

    public UserUpdateDto(String userName, String userEmail, String password, String loginId) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.loginId = loginId;
        this.updateAt = Instant.now();
    }

    private BinaryContent attachProfile;

}
