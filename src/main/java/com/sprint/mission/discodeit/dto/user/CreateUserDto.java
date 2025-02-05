package com.sprint.mission.discodeit.dto.user;

import java.util.Optional;

public record CreateUserDto (
    String username,
    String password,
    String email,
    String nickname,
    String phoneNumber,
    byte[] profileImage,
    String imageName,
    String fileType,
    String description){
}
