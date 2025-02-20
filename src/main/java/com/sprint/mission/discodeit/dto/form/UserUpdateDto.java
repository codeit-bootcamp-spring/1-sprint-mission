package com.sprint.mission.discodeit.dto.form;

import com.sprint.mission.discodeit.dto.entity.BaseEntity;
import com.sprint.mission.discodeit.dto.entity.BinaryContent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

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
