package com.sprint.mission.discodeit.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDTO {
    private String username;
    private String email;
    private String password;
    private byte[] profileImage;
}
