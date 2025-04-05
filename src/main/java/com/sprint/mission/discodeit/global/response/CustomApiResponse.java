package com.sprint.mission.discodeit.global.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sprint.mission.discodeit.global.exception.ErrorResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@JsonInclude(value = Include.NON_NULL)
public class CustomApiResponse<T> {

  @JsonIgnore
  private HttpStatus httpStatus;
  private boolean success;
  private String message;
  @Nullable
  private T data;
  @Nullable
  private ErrorResponse error;

  public static <T> CustomApiResponse<T> success(T data) {
    return CustomApiResponse.<T>builder()
        .httpStatus(HttpStatus.OK)
        .success(true)
        .data(data)
        .build();
  }

  public static CustomApiResponse<Void> success(String message) {
    return CustomApiResponse.<Void>builder()
        .httpStatus(HttpStatus.OK)
        .success(true)
        .message(message)
        .build();
  }

  public static <T> CustomApiResponse<T> created(T data) {
    return CustomApiResponse.<T>builder()
        .httpStatus(HttpStatus.CREATED)
        .success(true)
        .data(data)
        .build();
  }

//  public static <T> CustomApiResponse<T> failure(ErrorResponse errorResponse) {
//    return CustomApiResponse.<T>builder()
//        .success(false)
//        .error(errorResponse)
//        .build();
//  }

}
