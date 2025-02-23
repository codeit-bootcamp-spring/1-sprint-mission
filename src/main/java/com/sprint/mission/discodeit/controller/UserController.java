
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.FindUserResponseDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequestDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

// 사용자 관리 controller
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자 등록
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<FindUserResponseDto> createUser(@RequestParam("email") String email,
                                                          @RequestParam("password") String password,
                                                          @RequestParam("name") String name,
                                                          @RequestParam("nickname") String nickname,
                                                          @RequestParam("phoneNumber") String phoneNumber,
                                                          @RequestParam("profileImageFile") MultipartFile profileImageFile) throws IOException {

        CreateUserRequestDto createUserDto = new CreateUserRequestDto(email, password, name, nickname, phoneNumber, profileImageFile);
        UUID id = userService.create(createUserDto);

        return ResponseEntity.created(URI.create("/api/user/" + id)).body(userService.find(id));
    }

    // 사용자 다건 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<FindUserResponseDto>> findAllUser() {
        return ResponseEntity.ok(userService.findAll());
    }

    // 사용자 수정
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<FindUserResponseDto> updateUser(@PathVariable UUID id,
                                                          @RequestParam("email") String email,
                                                          @RequestParam("password") String password,
                                                          @RequestParam("name") String name,
                                                          @RequestParam("nickname") String nickname,
                                                          @RequestParam("phoneNumber") String phoneNumber,
                                                          @RequestParam("profileImageFile") MultipartFile profileImageFile) throws IOException {
        UpdateUserRequestDto updateUserDto = new UpdateUserRequestDto(email, password, name, nickname, phoneNumber, profileImageFile);

        userService.updateUser(id, updateUserDto);

        return ResponseEntity.ok(userService.find(id));
    }

    // 사용자 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 유저 온라인 상태 업데이트
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/online")
    public ResponseEntity<FindUserResponseDto> updateOnline(@PathVariable UUID id) {
        FindUserResponseDto findUserResponseDto = userStatusService.updateByUserId(id);
        return ResponseEntity.ok(findUserResponseDto);
    }
}
