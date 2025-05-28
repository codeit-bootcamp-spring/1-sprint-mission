package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth API", description = "Auth 관련 API")
public interface AuthApiDocs {

//    @Operation(summary = "유저 로그인", description = "유저 로그인")
//    @ApiResponses({
//        @ApiResponse(responseCode = "200", description = "로그인 성공"),
//        @ApiResponse(responseCode = "400", description = "로그인 실패")
//    })
//    ResponseEntity<UserResponse> login(UserRequest.Login userRequestLogin);
}
