package com.sprint.mission.dto.request;

import com.sprint.mission.entity.addOn.BinaryProfileContent;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
public class UserDtoForRequest {

    private String username;
    private String password;
    private String email;
    // 프로필 이미지
    private MultipartFile profileImg;

    //테스트용 생성자
    public UserDtoForRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserDtoForRequest(String username, String password, String email, MultipartFile profileImg) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileImg = profileImg;
    }

    

    public byte[] getProfileImgAsByte(){
        if (profileImg == null){
            return null;
        }

        try {
            return profileImg.getBytes();
        } catch (IOException e) {
            return null;
        }
    }


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
