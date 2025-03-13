
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.user.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.FindUserResponseDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

// 사용자 관리 controller
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자 등록
    @PostMapping
    public ResponseEntity<FindUserResponseDto> create(@RequestBody CreateUserRequestDto createUserRequestDto) throws IOException {

        UUID id = userService.create(createUserRequestDto);

        return ResponseEntity.created(URI.create("/api/user/" + id)).body(FindUserResponseDto.fromEntity(userService.find(id)));
    }

    // 사용자 다건 조회
    @GetMapping
    public ResponseEntity<List<FindUserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // 사용자 수정
    @PutMapping("/{id}")
    public ResponseEntity<FindUserResponseDto> updateUser(@PathVariable UUID id,
                                                          @RequestBody UpdateUserRequestDto updateUserRequestDto) throws IOException {

        userService.updateUser(id, updateUserRequestDto);

        return ResponseEntity.ok(FindUserResponseDto.fromEntity(userService.find(id)));
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 유저 온라인 상태 업데이트
    @PutMapping("/{id}/online")
    public ResponseEntity<FindUserResponseDto> updateOnline(@PathVariable UUID id) {
        FindUserResponseDto findUserResponseDto = userStatusService.updateByUserId(id);
        return ResponseEntity.ok(findUserResponseDto);
    }
}