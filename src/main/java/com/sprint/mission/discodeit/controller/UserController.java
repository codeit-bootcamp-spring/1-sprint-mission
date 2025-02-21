
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userDto.CreateUserRequestDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// 사용자 관리 controller
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자 등록
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam("name") String name,
                                             @RequestParam("nickname") String nickname,
                                             @RequestParam("phoneNumber") String phoneNumber,
                                             @RequestParam("profileImageFile")MultipartFile profileImageFile) throws IOException {

        CreateUserRequestDto createUserDto = new CreateUserRequestDto(email, password, name, nickname, phoneNumber, profileImageFile);
        UUID id = userService.create(createUserDto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "created");
        response.put("statusCode", 201);
        response.put("user", userService.find(id));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
