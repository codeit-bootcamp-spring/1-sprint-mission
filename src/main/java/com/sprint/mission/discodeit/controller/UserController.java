package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.users.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.users.create.UserCreateRequestWrapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.users.create.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.users.update.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.users.update.UserUpdateRequestWrapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST,value = "/create")
    public ResponseEntity<UserDto> CreateUser(@RequestBody UserCreateRequestWrapper userCreateRequestWrapper){
        UserCreateRequest userCreateRequest = userCreateRequestWrapper.userCreateRequest();
        Optional<BinaryContentCreateRequest> profileCreateRequest = userCreateRequestWrapper.profileCreateRequest();

        User createUser = userService.create(userCreateRequest, profileCreateRequest);
        URI location = URI.create("/users/" + createUser.getId());
        return ResponseEntity.created(location).body(userService.find(createUser.getId()));
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/update/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable UUID userId,@RequestBody UserUpdateRequestWrapper userUpdateRequestWrapper){
        UserUpdateRequest userUpdateRequest = userUpdateRequestWrapper.userUpdateRequest();
        Optional<BinaryContentCreateRequest> binaryContentCreateRequest = userUpdateRequestWrapper.profileCreateRequest();

        User updateUser = userService.update(userId, userUpdateRequest, binaryContentCreateRequest);
        return ResponseEntity.ok(userService.find(userId));
    }
    @RequestMapping(method = RequestMethod.DELETE,value = "/delete/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId){
        try {
            // 서비스 계층에서 delete 메서드 호출
            userService.delete(userId);
            // 삭제 완료 후 No Content 반환
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            // 사용자가 없으면 404 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            // 그 외의 예외는 500 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    public ResponseEntity<List<UserDto>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}/status-update")
    public ResponseEntity<UserStatus> updateStatusByUserId(@PathVariable UUID userId){
        Instant currentTime = Instant.now();
        UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(currentTime);
        return ResponseEntity.ok(userStatusService.updateByUserId(userId, userStatusUpdateRequest));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user-statuses/{statusId}")
    public ResponseEntity<UserStatus> updateStatusByStatusId(@PathVariable UUID statusId){
        Instant currentTime = Instant.now();
        UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(currentTime);
        return ResponseEntity.ok(userStatusService.update(statusId, userStatusUpdateRequest));
    }
}
