package com.sprint.mission.service.dto.request;

import com.sprint.mission.entity.addOn.BinaryProfileContent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDtoForRequest {

    private String username;
    private String password;
    private String email;
    // 프로필 이미지
    private BinaryProfileContent profileImg;
//
//    public UserDtoForRequest(String username, String password, String email) {
//        this.username = username;
//        this.password = password;
//        this.email = email;
//    }


    @Override
    public String toString() {
        return "UserDtoForRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", profileImg=" + profileImg +
                '}';
    }
}
