package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.status.AccountStatus;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import lombok.Getter;

@Getter
public class User extends BaseUpdatableEntity {

  //로그인 아이디
  private String username;
  //닉네임
  private String nickname;
  //이메일 - 로그인용 계정 아이디
  private String email;
  //비밀번호
  private String password;
  //사용자 설정 상태 메세지
  private String statusMessage;
  //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
  private AccountStatus accountStatus;
  //사용자 프로필 사진
  private BinaryContent profile;
  //유저 상태
  private UserStatus userStatus;

  public User(String username, String nickname, String email, String password, String statusMessage,
      AccountStatus accountStatus, BinaryContent profile) {
    this.username = username;
    this.nickname = nickname;
    this.email = email;
    this.password = password;
    this.statusMessage = statusMessage;
    this.accountStatus = accountStatus;
    this.profile = profile;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public void setProfile(BinaryContent profile) {
    this.profile = profile;
  }

  public void setUserStatus(UserStatus userStatus) {
    this.userStatus = userStatus;
  }

  public boolean isUpdated(UpdateUserDto updateUserDto) {
    if (updateUserDto == null) {
      return false;
    }

    boolean isUpdated = false;

    if (!username.equals(updateUserDto.newUsername()) && updateUserDto.newUsername() != null
        && !updateUserDto.newUsername().isEmpty()) {
      setNickname(updateUserDto.newUsername());
      isUpdated = true;
    }

    if (!nickname.equals(updateUserDto.newNickname()) && updateUserDto.newNickname() != null
        && !updateUserDto.newNickname().isEmpty()) {
      setNickname(updateUserDto.newNickname());
      isUpdated = true;
    }

    if (!email.equals(updateUserDto.newEmail()) && updateUserDto.newEmail() != null
        && !updateUserDto.newEmail().isEmpty()) {
      setEmail(updateUserDto.newEmail());
      isUpdated = true;
    }

    if (!password.equals(updateUserDto.newPassword()) && updateUserDto.newPassword() != null
        && !updateUserDto.newPassword().isEmpty()) {
      setPassword(updateUserDto.newPassword());
      isUpdated = true;
    }

    if (!accountStatus.equals(updateUserDto.accountStatus())
        && updateUserDto.accountStatus() != null) {
      setAccountStatus(updateUserDto.accountStatus());
      isUpdated = true;
    }

    if (statusMessage == null || (!statusMessage.equals(updateUserDto.newStatusMessage())
        && updateUserDto.newStatusMessage() != null)) {
      setStatusMessage(updateUserDto.newStatusMessage());
      isUpdated = true;
    }

    return isUpdated;
  }
}
