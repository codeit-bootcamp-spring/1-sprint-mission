package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserFindDTO
        (UUID id, String name, String email,
         Boolean isOnline, String filePath){
}
