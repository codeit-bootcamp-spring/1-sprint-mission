package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.Email;
import com.sprint.mission.discodeit.common.Name;
import com.sprint.mission.discodeit.common.Password;
import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.dto.UserReqDTO;
import com.sprint.mission.discodeit.enums.RegionCode;
import com.sprint.mission.discodeit.enums.UserType;
import lombok.Getter;

import java.util.UUID;

/* # User
 * - 사용자 객체
 * -- 객체 생성의 책임
 */

@Getter
public class User {
    private UUID id;    // 고유 번호
    private Name userName;  // ID와 같은 역할
    private Name nickname;  // 별명 (사용자)
    private Email email;    // 이메일
    private Password password;    // 비밀번호 -> 추후에는 암호화 되어 저장된다고 생각하기
    private Phone phone;    // 전화 번호
    private UserType userType;  // 기본은 Common
    private boolean status; // 활성화(1)/비활성화(0) 상태 -> 생성 시 default로 1
    private String userImgPath;  //
    private String introduce;   // 자기 소개 (Nullable)
    private long createdAt; // 생성 시간
    private long updatedAt; // 업데이트 시간
    // 닉네임 등 서버 내 설정은 Profile로 따로 빼야 함

    // 생성자
    public User(UserReqDTO userReqDTO) {
        id = UUID.randomUUID();
        this.userName = new Name(userReqDTO.getUserName());
        this.nickname = new Name(userReqDTO.getNickname());
        this.email = new Email(userReqDTO.getEmail());
        this.password = new Password(userReqDTO.getPassword());
        this.phone = new Phone(userReqDTO.getPhone(), userReqDTO.getRegionCode());
        this.userType = UserType.COMMON;    // BOT은 어떻게 생성되는지 모른다.. 일단 COMMON으로 정해둠
        this.status = true;
        this.userImgPath = userReqDTO.getImgPath();
        createdAt = System.currentTimeMillis();
        updatedAt = System.currentTimeMillis(); // 정의해두고 비워두기엔 너무 그럼 업데이트때 다시 불러오면 될 듯
        this.introduce = userReqDTO.getIntroduce();
    }

    // Update (뭐 Setter이긴 한데 Update라는 의미를 강조하고 싶음)
    // TODO: updatedAt -> 내부 공통 메서드 처리 고려하기
    public void updateUserName(String userName) {
        // 사용자명(userName) 업데이트 - 고유한 값이기 때문에 불변 객체로 설정
        this.userName = new Name(userName);
        updatedAt = System.currentTimeMillis();
    }

    public void updateNickname(String nickname) {
        this.nickname.setName(nickname);
        updatedAt = System.currentTimeMillis();
    }

    public void updateEmail(String email) {
        this.email.changeEmailAddr(email);
        updatedAt = System.currentTimeMillis();
    }

    public void updatePhone(String phoneNum, String regionCode) {
        this.phone.setPhoneNum(phoneNum);
        this.phone.setRegionCode(RegionCode.fromString(regionCode));
        updatedAt = System.currentTimeMillis();
    }

    public void updateUserType(UserType userType) {
        this.userType = userType;
        updatedAt = System.currentTimeMillis();
    }

    public void updateStatus(boolean status) {
        this.status = status;
        updatedAt = System.currentTimeMillis();
    }

    public void updateUserImg(String userImgPath) {
        this.userImgPath = userImgPath;
        updatedAt = System.currentTimeMillis();
    }

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
        updatedAt = System.currentTimeMillis();
    }
}
