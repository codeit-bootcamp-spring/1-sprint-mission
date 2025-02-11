package com.sprint.mission.discodeit.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
    private String username;
    private String email;
    private String password;
    private String profileImage;
}
