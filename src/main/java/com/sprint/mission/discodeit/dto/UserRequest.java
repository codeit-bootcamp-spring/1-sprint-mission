package com.sprint.mission.discodeit.dto;

public record UserRequest(
    String name,
    String email,
    String password
) {

  public record Login(
      String name,
      String password
  ) {

  }
}
