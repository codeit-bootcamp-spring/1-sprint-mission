package com.sprint.mission.discodeit.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthServiceLoginDTO {
    String username;
    String password;
}
