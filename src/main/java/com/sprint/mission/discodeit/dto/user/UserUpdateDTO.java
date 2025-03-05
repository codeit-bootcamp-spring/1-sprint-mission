package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

  private String newUsername;
  private String newEmail;
  private String newPassword;
}
