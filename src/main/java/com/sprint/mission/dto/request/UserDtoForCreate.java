package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDtoForCreate(
        @NotBlank(message = "유저 이름은 필수입니다.")
        @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하로 입력해주세요.")
        String username,
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email) {

    public User toEntity() {
        return new User(username, password, email, null);
    }
}
;
// 프로필 이미지
//    private MultipartFile profileImg;
//
//    public byte[] getProfileImgAsByte(){
//        if (profileImg == null){
//            return null;
//        }
//
//        try {
//            return profileImg.getBytes();
//        } catch (IOException e) {
//            return null;
//        }
//    }

