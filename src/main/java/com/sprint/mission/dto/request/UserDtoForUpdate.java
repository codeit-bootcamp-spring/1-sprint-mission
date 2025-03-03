package com.sprint.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.entity.main.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Schema(description = "USER 수정 정보 DTO")
public record UserDtoForUpdate(

        @Schema(description = "새로운 이름", example = "홍길동")
        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하로 입력해주세요.")
        String newName,

        @Schema(description = "새로운 비밀번호", example = "1234")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String newPassword,
//
        @Schema(description = "새로운 이메일", example = "code123@codeit.com")
        @Email(message = "이메일 형식이 아닙니다.")
        String newEmail) {

    public User toUpdateEntity(User user) {
        user.setName(newName);
        user.setPassword(newPassword);
        user.setEmail(newEmail);
        user.refreshUpdateAt();
        return user;
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

