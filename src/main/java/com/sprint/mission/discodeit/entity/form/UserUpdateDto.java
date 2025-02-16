package com.sprint.mission.discodeit.entity.form;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
public class UserUpdateDto extends BaseEntity {

    @NotEmpty
    private String userName;
    @NotEmpty
    private String userEmail;
    @NotEmpty
    private transient String password;
    @NotEmpty
    private String loginId;

    public UserUpdateDto(String userName, String userEmail, String password, String loginId) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.loginId = loginId;
    }

    private BinaryContent attachProfile;

}
