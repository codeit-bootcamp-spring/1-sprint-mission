package com.sprint.mission.discodeit.controller.user;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.*;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDTO;
import com.sprint.mission.discodeit.dto.response.user.UserDTO;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.interfacepac.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    public UserController(UserService userService, UserStatusService userStatusService) {
        this.userService = userService;

        this.userStatusService = userStatusService;
    }

    //사용자 등록
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public UserDTO createUser(
            @RequestPart(name = "user-create-dto") UserCreateDTO userCreateDTO,
            @RequestPart(name = "binary-content-create-request") MultipartFile binaryContentCreateRequest
            ) {
        BinaryContentCreateRequest binaryContent = null;
        if (Objects.nonNull(binaryContentCreateRequest)) {
            try {
                binaryContent = new BinaryContentCreateRequest(
                        binaryContentCreateRequest.getOriginalFilename(),
                        binaryContentCreateRequest.getContentType(),
                        binaryContentCreateRequest.getBytes()
                );
            } catch (IOException exception) {
                throw new IllegalArgumentException(exception);
            }
        }
        return userService.create(userCreateDTO, binaryContent);
    }

    //사용자 정보 수정
    @PatchMapping
    public UserDTO updateUser(@RequestBody UserUpdateRequestDTO updateRequestDTO) {
        return userService.update(updateRequestDTO.userUpdateDTO()
                ,updateRequestDTO.userUpdateProfileImageDTO());
    }

    //사용자 삭제
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }

    //모든 사용자 조회
    @GetMapping("/findAll")
    public List<UserDTO> getUsers() {
        return userService.findAll();
    }

    //사용자의 온라인 상태 업데이트
    @PatchMapping("/status/update/{userId}")
    public UserStatusResponseDTO updateUserStatus(@PathVariable UUID userId) {
        return userStatusService.updateByUserId(userId);
    }



}
