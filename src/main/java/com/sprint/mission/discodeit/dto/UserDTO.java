package com.sprint.mission.discodeit.dto;

import java.util.UUID;
public record UserDTO() {

    //userCreateDTO : 사용자 등록
    public static class UserCreateDTO {
        private String username;
        private String userEmail;
        private String password;
    }

    //userResponseDto : 사용자 조회
    public static class UserResponseDTO {
        private UUID userId;
        private String userName;
        private String userEmail;
        private boolean onlineStatus;
    }

    //userUpdateDTO : 사용자 정보 수정
    public static class UserUpdateDTO {
        private UUID userId;
        private String userName;
        private String userEmail;
        private String password;
    }
    public static class deleteUserDTO {
        private UUID userId;
    }
}