package com.sprint.mission.discodeit.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsersDto {
    private String id;
    private String name;
    private String email;
    private String password;
    private boolean online = false;
    private String profileImage;
}