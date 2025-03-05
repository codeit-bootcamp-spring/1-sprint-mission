package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.dto.AuthDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.exception.ExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "Auth 관련 API (회원가입, 로그인, 로그아웃 등)")
public interface AuthControllerDocs {

    @Operation(summary = "로그인", description = "사용자 인증 정보를 검증하고, 성공 시 사용자 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success",
                                            value = "{\n  \"success\": true,\n  \"data\": {\n    \"id\": 1,\n    \"uuid\": \"8c9115f7-8b7d-4bfc-b8ea-8af103e3bf32\",\n    \"username\": { \"name\": \"username1\" },\n    \"nickname\": { \"name\": \"nickname1\" },\n    \"email\": { \"email\": \"mail1@mail.com\", \"verified\": false },\n    \"online\": true\n  },\n  \"error\": null\n}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 인증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<UserDTO.response> login(@RequestBody AuthDTO.loginReq loginDto);
}
