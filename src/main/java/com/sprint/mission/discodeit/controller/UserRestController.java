package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User API" , description = "유저 관리 API")
public class UserRestController {
    private final UserService userService;

    @Operation(summary = "User create", description = "유저 등록시 사용")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public UserResponse userCreate(@Valid @RequestPart(value = "request", required = true) @NotNull @JsonProperty UserRequest request,
                                   @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        log.info("user create request : {}", request);
        return userService.create(request, file);
    }

    @Operation(summary = "User list", description = "유저 리스트")
    @GetMapping
    public List<UserResponse> userList(){
        log.info("user list");
        return userService.readAll();
    }

    @Operation(summary = "User find", description = "유저 검색")
    @GetMapping("/{id}")
    public UserResponse findByUser(@PathVariable UUID id){
        log.info("user find request : {}", id);
        return userService.readOne(id);
    }


    @Operation(summary = "User update", description = "유저 업데이트")
    @PutMapping(path="/{id}")
    public UserResponse UserUpdate(@PathVariable UUID id,
                                   @RequestPart(value = "request", required = true) @NotNull @JsonProperty UserRequest request){
//                                   @RequestBody UserRequest request){
        log.info("user update request : {}", request);
        return userService.update(id, request);
    }

    @Operation(summary = "User delete", description = "유저 삭제")
    @DeleteMapping("/{id}")
    public boolean UserDelete(@PathVariable UUID id){
        log.info("user delete request : {}", id);
        return userService.delete(id);
    }
}
