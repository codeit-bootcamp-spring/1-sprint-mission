package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.User;

public record UserDtoForCreate(
    String username,
    String password,
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

