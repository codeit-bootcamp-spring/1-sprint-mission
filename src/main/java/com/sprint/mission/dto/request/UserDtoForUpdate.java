package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.User;
import java.time.Instant;

public record UserDtoForUpdate(
    String newName,
    String newPassword,
    String newEmail) {

  public User toUpdateEntity(User user){
    user.setName(newName);
    user.setPassword(newPassword);
    user.setEmail(newEmail);
    user.setUpdateAt(Instant.now());
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

