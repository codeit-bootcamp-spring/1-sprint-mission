package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.FindUserResponse;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByUserIdRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
public interface UserApi {

  @Operation(summary = "User 등록")
  ResponseEntity<UserDto> createUser(
      @Parameter(
          description = "User 생성 정보",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
      ) CreateUserRequest createUserRequest,
      @Parameter(
          description = "User 프로필 이미지",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
      ) MultipartFile profileImage
  );

  @Operation(summary = "User 정보 수정")
  ResponseEntity<UserDto> updateUser(
      @Parameter(description = "수정할 User ID") UUID userId,
      @Parameter(description = "수정할 User 정보") UpdateUserRequest updateUserRequest,
      @Parameter(description = "수정할 User 프로필 이미지") MultipartFile profileImage
  );

  @Operation(summary = "User 삭제")
  ResponseEntity<Void> deleteUser(
      @Parameter(description = "삭제할 User ID") UUID userId
  );

  @Operation(summary = "전체 User 목록 조회")
  ResponseEntity<List<FindUserResponse>> findAllUsers();

  @Operation(summary = "User 온라인 상태 업데이트")
  ResponseEntity<UserStatusDto> updateUserStatusByUserId(
      @Parameter(description = "상태를 변경할 User ID") UUID userId,
      @Parameter(description = "변경할 User 온라인 상태 정보")
      UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest
  );
}
