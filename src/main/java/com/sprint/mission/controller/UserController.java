package com.sprint.mission.controller;

import com.sprint.mission.dto.request.UserDtoForRequest;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final JCFUserService userService;
    private final UserStatusService userStatusService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody UserDtoForRequest requestDTO) {
        userService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FindUserDto> findById(@RequestParam UUID userId){
        User findUser = userService.findById(userId);
        Boolean isOnline = userStatusService.findById(userId)
                .map(UserStatus::isOnline)
                .orElse(false);
        return ResponseEntity.status(HttpStatus.OK).body(new FindUserDto(findUser, isOnline));
    }

    @GetMapping("/users")
    public ResponseEntity<List<FindUserDto>> findAll(){
        Map<User, UserStatus> statusMapByUser = userStatusService.findStatusMapByUserList(userService.findAll());
        List<FindUserDto> userListDTO = new ArrayList<>();
        for (User user : statusMapByUser.keySet()) {
            userListDTO.add(new FindUserDto(user, statusMapByUser.get(user).isOnline()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(userListDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@RequestParam UUID userId){
        return ResponseEntity.ok("gd");
    }




}
