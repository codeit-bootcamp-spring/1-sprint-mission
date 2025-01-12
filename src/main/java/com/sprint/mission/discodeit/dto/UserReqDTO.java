package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.RegionCode;

import java.util.Objects;

/* # UserReqDTO
 * - 사용자 Req DTO
 * -- 무언가 사용자 관련 요청 시 (주로 생성 요청)
 * -- 객체 생성의 책임
 */

public class UserReqDTO implements UserDTO {
    private String userName;
    private String nickname;
    private String email;
    private String passwd;
    private RegionCode regionCode;  // 문자열로 전달 후 내부에서 변환
    private String phone;
    private String imgPath;
    private String introduce;

    // 기본 생성자 및 Getter/Setter
    public UserReqDTO(String userName, String nickname, String email, String passwd, String regionCode, String phone, String imgPath) {
        this.userName = Objects.requireNonNull(userName, "userName cannot be null");
        this.nickname = Objects.requireNonNull(nickname, "nickname cannot be null");
        this.email = Objects.requireNonNull(email, "email cannot be null");
        this.passwd = Objects.requireNonNull(passwd, "password cannot be null");
        this.regionCode = Objects.requireNonNull(RegionCode.fromString(regionCode), "regionCode cannot be null");
        this.phone = Objects.requireNonNull(phone, "phone cannot be null");
        this.imgPath = Objects.requireNonNullElse(imgPath, "defaultImg.png");  // 필수는 아닌데 null이면 기본 이미지 path 넣어주기
        this.introduce = Objects.requireNonNullElse(introduce, ""); // 필수는 아니니까 null이면 ""로 문자열 초기화 시키기
    }

    // Getter
    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return passwd;
    }

    public RegionCode getRegionCode() {
        return regionCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getIntroduce() {
        return introduce;
    }
}