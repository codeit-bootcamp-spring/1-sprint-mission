package com.sprint.mission.discodeit.entity.user.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserCreateWithBinaryContentResponse {

  private UUID id;
  private String userName;
  private String userEmail;

  @Setter
  private String fileName;
}
