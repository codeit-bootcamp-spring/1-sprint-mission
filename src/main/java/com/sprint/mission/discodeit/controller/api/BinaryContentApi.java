package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentApi {

    @Operation(
            summary = "BinaryContent 조회",
            description = "ID 값으로 해당 BinaryContent 조회합니다.",
            parameters = @Parameter(
                    name = "binaryContentId",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
    )
    ResponseEntity<BinaryContent> find(UUID binaryContentId);

    @Operation(
            summary = "BinaryContent 복수 조회",
            description = "쿼리 파라미터로 여러 BinaryContent 조회",
            parameters = @Parameter(
                    name = "binaryContentIds"
            )
    )
    ResponseEntity<List<BinaryContent>> findAllByIdIn(List<UUID> binaryContentIds);

}
