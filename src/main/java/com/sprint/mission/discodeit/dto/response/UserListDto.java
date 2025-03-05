package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.UserDto;
import java.util.List;

public record UserListDto(
    List<UserDto> dtos,
    int totalCount //총 유저수
) {

  public static UserListDto from(List<UserDto> dtos) {
    return new UserListDto(dtos, dtos.size());
  }
}