package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final AuthService authService;


    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestPart(value = "userCreateRequest") UserCreateRequest userCreateRequest,
            // multipart/form-data 형식으로 요청보낼 때 파일의 키 이름을 "binaryContent"
            @RequestPart(value = "binaryContent", required = false) MultipartFile file) throws Exception {

        BinaryContentCreateRequest binaryContentCreateRequest;
        if(file != null){
            binaryContentCreateRequest = new BinaryContentCreateRequest(file);
        }else {
            binaryContentCreateRequest = null;
        }

        UserCreateResponse userCreateResponse = userService.createUser(userCreateRequest, binaryContentCreateRequest);
        // .ok 응답코드 200 + body
        return ResponseEntity.ok(userCreateResponse);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateRequest userUpdateRequest){
        userService.updateUserInfo(userId, userUpdateRequest);
        // ResponseEntity.noContent().build() → 204 No Content
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID id){
        userService.removeUserById(id);
        // ResponseEntity.noContent().build() → 204 No Content
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<UserFindResponse>> findAllUsers(){
        return ResponseEntity.ok(userService.showAllUsers());
    }


    @RequestMapping(value = "/{userId}/status", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateUserStateByUserId(@PathVariable UUID userId, @RequestBody UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest){
        userStatusService.updateUserStatusByUserId(userId, userStatusUpdateByUserIdRequest);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = authService.login(userLoginRequest);
        return ResponseEntity
                .ok(userLoginResponse);
    }

}
