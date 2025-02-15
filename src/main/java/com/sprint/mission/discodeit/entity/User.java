package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@ToString
public class User extends BaseEntity implements Serializable {
    private String userName;
    private String userEmail;
    private List<Channel> channelList; // 유저가 소유하고 있는 ChannelList
    private String password;

    public User(UserCreateDTO dto) {
        super();
        this.userName = dto.getUserName();
        this.userEmail = dto.getUserEmail();
        this.password = dto.getPassword();
    }

    public void updateName(String userName) {
        if(userName == null || userName.equals(this.userName)) return;
        this.userName = userName;
        updateTime(Instant.now());
    }
    public void updateEmail(String userEmail) {
        if(userEmail == null || userEmail.equals(this.userEmail))return;
        this.userEmail = userEmail;
        updateTime(Instant.now());
    }
    public void updatePassword(String password) {
        if(password == null || password.equals(this.password))return;
        this.password = password;
        updateTime(Instant.now());
    }
    public void update(UserUpdateDTO dto) {
        if(dto.getUserName() != null && !dto.getUserName().equals(this.userName)){
            this.userName = dto.getUserName();
        }
        if(dto.getUserEmail() != null && !dto.getUserEmail().equals(this.userEmail)){
            this.userEmail = dto.getUserEmail();
        }
        if(dto.getPassword() != null && !dto.getPassword().equals(this.password)) {
            this.password =dto.getPassword();
        }
        updateTime(Instant.now());
    }

}
