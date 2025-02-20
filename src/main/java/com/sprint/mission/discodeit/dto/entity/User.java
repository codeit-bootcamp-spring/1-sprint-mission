package com.sprint.mission.discodeit.dto.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;



@Data
public class User extends BaseEntity implements Serializable {

    @NotEmpty
    private String userName;
    @NotEmpty
    private String userEmail;
    @NotEmpty
    private transient String password;
    @NotEmpty
    private String loginId;

    private BinaryContent attachProfile;

    public User(String loginId, String password, String userName, String userEmail) {
        super();
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.userEmail = userEmail;
    }
    public User(String loginId, String password, String userName, String userEmail,BinaryContent attachProfile) {
        super();
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.userEmail = userEmail;
        this.attachProfile = attachProfile;
    }
}
