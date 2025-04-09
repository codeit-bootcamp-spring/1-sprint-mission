package com.sprint.mission.discodeit.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto{
    UUID id;
    String username;
    String email;
    BinaryContentDto profile;
    Boolean online;
        }
