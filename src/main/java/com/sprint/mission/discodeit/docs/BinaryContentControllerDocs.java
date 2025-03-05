package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.exception.ExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "BinaryContents", description = "Binary Content 관련 API/ 파일 관련 API")
public interface BinaryContentControllerDocs {

    @Operation(summary = "프로필 조회", description = "주어진 referenceId에 해당하는 프로필 이미지를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 이미지 조회 성공",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(type = "string", format = "binary")),
                            @Content(mediaType = "image/png", schema = @Schema(type = "string", format = "binary"))
                    }),
            @ApiResponse(responseCode = "404", description = "프로필 이미지가 존재하지 않음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    ResponseEntity<byte[]> getProfileByUserId(@RequestParam UUID userId) throws IOException;

    @Operation(summary = "메시지 파일 압축 및 다운로드", description = "주어진 referenceId에 해당하는 메시지 첨부 파일들을 압축하여 zip 파일로 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 압축 및 다운로드 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    ResponseEntity<byte[]> getContentsByMessageId(@RequestParam UUID messageId) throws IOException;
}
