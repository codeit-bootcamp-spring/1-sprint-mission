package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResultDTO <T>{
    private Integer code;
    private String message;
    private T data;
}
