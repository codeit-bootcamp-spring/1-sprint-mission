package com.sprint.mission.discodeit.entity.user.dto;


import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateResponse {

  UUID id;
  String username;
  String email;
  BinaryContentDto profile;
  Boolean online;
}
