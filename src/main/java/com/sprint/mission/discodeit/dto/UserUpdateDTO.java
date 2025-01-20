package com.sprint.mission.discodeit.dto;

import lombok.Getter;

@Getter
public class UserUpdateDTO implements UserDTO {
    private String userName;
    private String nickname;
    private String email;
    private String userType;
    private String regionCode;  // 문자열로 전달 후 내부에서 변환
    private String phone;
    private String imgPath;
    private String introduce;

    public UserUpdateDTO(String userName, String nickname, String email, String userType, String regionCode, String phone, String imgPath, String introduce) {
        this.userName = userName;
        this.nickname = nickname;
        this.email = email;
        this.userType = userType;
        this.regionCode = regionCode;
        this.phone = phone;
        this.imgPath = imgPath;
        this.introduce = introduce;
    }
}
