package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Read Status API",
        description = "읽음 상태 API"
)
public interface ReadStatusApi {

    @Operation(
            summary = "읽음 상태 생성",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReadStatusCreateRequest.class)
                    )
            )
    )
    ResponseEntity<ReadStatus> create(ReadStatusCreateRequest request);

    @Operation(
            summary = "읽음 상태 수정",
            parameters = @Parameter(name = "readStatusId", required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReadStatusUpdateRequest.class)
                    )
            )
    )
    ResponseEntity<ReadStatus> update(UUID readStatusId, ReadStatusUpdateRequest request);

    @Operation(
            summary = "유저의 읽음 상태 조회",
            parameters = @Parameter(
                    name = "userId",
                    description = "유저 ID",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
    )
    ResponseEntity<List<ReadStatus>> findAllByUserId(UUID userId);

}
