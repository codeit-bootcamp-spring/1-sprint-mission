package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;


@Tag(name = "USER API", description = "User management operations")
public interface UserApiDocs {

  @Operation(summary = "Find single user", description = "Find single user using userId")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User of userId found"),
      @ApiResponse(
          responseCode = "404",
          description = "No such user of userId found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
      )
  })
  ResponseEntity<UserResponseDto> getUser(String id);
}
