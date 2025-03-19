package com.sprint.mission.discodeit.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponseDto<T> {

    private boolean loggedIn;
    private String userId;
}
