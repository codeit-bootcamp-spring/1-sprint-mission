package com.sprint.mission.discodeit.dto;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto implements Serializable {
    private String username;
    private String password;
}
