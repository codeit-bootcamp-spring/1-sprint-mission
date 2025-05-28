package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;


@Tag(name = "ReadStatus API", description = "Message 읽음 상태 작업")
public interface ReadStatusApiDocs {

  @Operation(summary = "ReadStatus 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "ReadStatus 생성 성공"),
      @ApiResponse(
          responseCode = "400",
          description = "해당 User + Channel 의 ReadStatus 가 이미 존재함",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel 혹은 User 를 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<ReadStatusResponseDto> createReadStatus(
      @Parameter(required = true, description = "ReadStatus 생성 정보") CreateReadStatusDto dto,
      @AuthenticationPrincipal UserDetails details);


  @Operation(summary = "ReadStatus 업데이트")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "ReadStatus 업데이트 성공"),
      @ApiResponse(
          responseCode = "404",
          description = "ReadStatus를 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<ReadStatusResponseDto> updateReadStatus(
      @Parameter(required = true, description = "업데이트 ReadStatus UUID") String id,
      @Parameter(required = true, description = "업데이트 정보") UpdateReadStatusDto dto,
      @AuthenticationPrincipal UserDetails details);


  @Operation(summary = "User의 ReadStatus 목록")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "ReadStatus 조회 성공")
  })
  ResponseEntity<List<ReadStatusResponseDto>> getUserReadStatus(
      @Parameter(required = true, description = "User UUID") String userId);
}
