package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User API", description = "유저 관리 API")
public interface UserControllerDocs {

  @Operation(summary = "유저 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "유저 생성 성공"),
      @ApiResponse(responseCode = "400", description = "중복된 이름 또는 이메일")
  })
  @PostMapping
  ResponseEntity<UserDto> createUser(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile
  );

  @Operation(summary = "모든 유저 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "모든 유저 조회 성공")
  })
  @GetMapping
  ResponseEntity<List<UserDto>> getUsers();

  @Operation(summary = "유저 수정")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "유저 수정 성공"),
      @ApiResponse(responseCode = "400", description = "중복된 이름 또는 이메일"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
  })
  @PatchMapping("/{id}")
  ResponseEntity<UserDto> updateUser(
      @PathVariable UUID id,
      @RequestPart UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile profile
  );

  @Operation(summary = "유저 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "유저 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")

  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteUser(
      @PathVariable UUID id
  );
}
