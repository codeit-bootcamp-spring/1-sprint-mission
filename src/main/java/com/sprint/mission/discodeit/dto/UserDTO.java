package com.sprint.mission.discodeit.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

public class UserDTO {
    @Builder
    public record createDTO(
            String userName,
            String email,
            String password
    ) {}

    @Builder
    public record responseDTO(UUID userId,
                              String userName,
                              String email,
                              boolean isOnline
    ) {}

    @Builder
    public record updateDTO(UUID userId,
                            String newUsername,
                            String newEmail,
                            String newPassword,
                            MultipartFile newProfileImage
    ) {}
}
