package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.enums.RegionCode;
import lombok.Getter;

/* # UserReqDTO
 * - 사용자 Req DTO
 * -- 무언가 사용자 관련 요청 시 (주로 생성 요청)
 * -- 객체 생성의 책임
 */

@Getter
public class UserReqDTO implements UserDTO {
    private String userName;
    private String nickname;
    private String email;
    private String password;
    private RegionCode regionCode;  // 문자열로 전달 후 내부에서 변환
    private String phone;
    private String imgPath;
    private String introduce;

    // 기본 생성자 및 Getter/Setter
    public UserReqDTO(String userName, String nickname, String email, String password, String regionCode, String phone, String imgPath) {
        this.userName = userName;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.regionCode = RegionCode.valueOf(regionCode);
        this.phone = phone;
        this.imgPath = imgPath;  // 필수는 아닌데 null이면 기본 이미지 path 넣어주기
        this.introduce = ""; // 필수는 아니니까 null이면 ""로 문자열 초기화 시키기
    }
}