package com.sprint.mission.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
public class UserDtoForRequest {

    private String username;
    private String password;
    private String email;
    // 프로필 이미지
    private MultipartFile profileImg;

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
}
