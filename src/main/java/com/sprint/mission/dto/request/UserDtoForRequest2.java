package com.sprint.mission.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@Setter
public class UserDtoForRequest2 {

    private String username;
    private String password;
    private String email;
    private MultipartFile profile;

    public UserDtoForRequest2() {}

    //테스트용 생성자
    public UserDtoForRequest2(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    @Override
    public String toString() {
        return "UserDtoForRequest2{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", profile=" + profile +
                '}';
    }
}
