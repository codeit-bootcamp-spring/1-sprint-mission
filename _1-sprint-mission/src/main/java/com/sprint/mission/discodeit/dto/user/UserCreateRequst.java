package com.sprint.mission.discodeit.dto.user;

//요청
public record UserCreateRequst(
    String username,
    String email,
    String password
) {

}
