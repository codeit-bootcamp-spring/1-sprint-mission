package com.sprint.mission.discodeit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

  private boolean success;
  private String message;

}
