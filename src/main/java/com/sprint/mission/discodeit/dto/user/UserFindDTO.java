package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserFindDTO {
    private UUID id;
    private String username;
    private String email;
    private boolean online;
    private Instant createAt;
    private Instant updateAt;
}
