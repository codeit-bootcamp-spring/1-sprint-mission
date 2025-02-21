package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserCreateDto {
    String username;
    String email;
    UserStatus status;
    MultipartFile profile;

}
