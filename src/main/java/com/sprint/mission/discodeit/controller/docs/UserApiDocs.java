package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.dto.request.UserStatusRequest;
import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User API", description = "User 관련 API")
public interface UserApiDocs {

    @Operation(summary = "모든 유저 정보", description = "모든 유저 정보 가져오기")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "모든 유저 정보 가져오기 성공"),
        @ApiResponse(responseCode = "400", description = "모든 유저 정보 가져오기 실패")
    })
    ResponseEntity<List<UserResponse>> getAllUser();

    @Operation(summary = "유저 생성", description = "유저 생성하기")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 생성 성공"),
        @ApiResponse(responseCode = "400", description = "유저 생성 실패")
    })
    ResponseEntity<UserResponse> createUser(
        UserRequest.Create userRequest,
        MultipartFile userProfileImage
    );

    @Operation(summary = "유저 정보 수정", description = "유저 정보 수정하기")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공"),
        @ApiResponse(responseCode = "400", description = "유저 정보 수정 실패"),
        @ApiResponse(responseCode = "404", description = "해당 유저가 존재하지 않습니다.")
    })
    ResponseEntity<UserResponse> updateUser(
        UUID userId,
        UserRequest.Update userRequest,
        MultipartFile userProfileImage
    );

    @Operation(summary = "유저 삭제", description = "유저 삭제 수정하기")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "유저 삭제 실패"),
    })
    ResponseEntity<Void> deleteUser(UUID userId);

    @Operation(summary = "유저 상태 업데이트", description = "유저 상태 업데이트(online or offline)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 상태 업데이트 성공"),
        @ApiResponse(responseCode = "400", description = "유저 상태 업데이트 실패, 잘못된 요청입니다."),
        @ApiResponse(responseCode = "404", description = "해당 유저가 존재하지 않습니다.")
    })
    ResponseEntity<UserStatusResponse> updateUserStatus(UUID userId,
        UserStatusRequest.Update request);

}
