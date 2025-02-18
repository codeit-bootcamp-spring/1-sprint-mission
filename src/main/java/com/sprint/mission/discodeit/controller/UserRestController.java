package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {
    private final UserService userService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public UserResponse userCreate(@RequestPart(value = "request", required = true) @NotNull @JsonProperty UserRequest request,
                                   @RequestPart(value = "file", required = false) MultipartFile file){

        return userService.create(request, file);
    }

    @GetMapping("/list")
    public List<UserResponse> userList(){
        return userService.readAll();
    }

    @GetMapping("/{id}")
    public UserResponse findByUser(@PathVariable UUID id){
        return userService.readOne(id);
    }

    //TODO update시 id값 변경되는 것 같음
    @PutMapping("/update/{id}")
    public UserResponse UserUpdate(@PathVariable UUID id,
                                  @RequestBody UserRequest request){
        return userService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public boolean UserDelete(@PathVariable UUID id){
        return userService.delete(id);
    }
}
