package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.exception.ExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "Read Status(읽기 상태) 관련 API")
public interface ReadStatusControllerDocs {

    @Operation(summary = "읽음 상태 생성", description = "새로운 읽음 상태를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "읽음 상태 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<CommonDTO.idResponse> createReadStatus(@RequestBody ReadStatusDTO.request readStatusReqDto);

    @Operation(summary = "읽음 상태 수정", description = "주어진 읽음 상태 ID의 마지막 읽은 시간을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "읽음 상태 업데이트 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<CommonDTO.idResponse> updateReadStatus(
            @Parameter(description = "읽음 상태 ID", required = true) @PathVariable Long readStatusId,
            @Parameter(description = "마지막 읽은 시간", required = true) @RequestParam("lastReadAt") Instant lastReadAt);

    @Operation(summary = "유저별 읽음 상태 조회", description = "주어진 유저 ID에 해당하는 읽음 상태 리스트를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "읽음 상태 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<List<ReadStatusDTO.response>> getReadStatusByUser(@Parameter(description = "유저 ID", required = true) @RequestParam UUID userId);
}
