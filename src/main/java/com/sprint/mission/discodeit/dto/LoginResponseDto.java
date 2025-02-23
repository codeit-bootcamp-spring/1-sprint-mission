package com.sprint.mission.discodeit.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto implements Serializable {
    private UUID userId;
    private String username;
    private String email;
}
