package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public record UserUpdateDto(
    String username,
    String password,
    String email,
    String nickname,
    String phoneNumber,
    String description,
    MultipartFile profileImage
) {}

