//package com.sprint.mission.discodeit.exception;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.time.Instant;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Schema(description = "예외 응답")
//public class ExceptionResponse {
//
//  @Schema(description = "오류 유형", example = "Not Found")
//  private String error;
//
//  @Schema(description = "오류 메시지", example = "User with id 123e4567-e89b-12d3-a456-426614174000 not found")
//  private String message;
//
//  @Schema(description = "오류 발생 시간")
//  private Instant timestamp;
//}