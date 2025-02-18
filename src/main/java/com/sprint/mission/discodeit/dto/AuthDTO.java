package com.sprint.mission.discodeit.dto;

import lombok.Builder;

public class AuthDTO {

    @Builder
    public record loginReq(String username, String password) {
    }
}
