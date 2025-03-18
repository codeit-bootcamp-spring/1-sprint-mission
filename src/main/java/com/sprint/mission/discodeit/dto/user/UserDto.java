package com.sprint.mission.discodeit.dto.user;


import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

  private UUID id;
  private String username;
  private String email;
  private BinaryContentDto profile;
  private boolean isOnline;
}
