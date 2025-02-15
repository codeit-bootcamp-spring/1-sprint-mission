package com.sprint.mission.controller;

import com.sprint.mission.dto.request.UserDtoForRequest;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.entity.addOn.BinaryProfileContent;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.jcf.addOn.BinaryProfileService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import com.sprint.mission.service.jcf.main.JCFChannelService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final JCFUserService userService;
    private final UserStatusService userStatusService;
    private final JCFChannelService channelService;
    private final BinaryProfileService binaryProfileService;


    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    @PostMapping
    public ResponseEntity<String> create(@RequestBody UserDtoForRequest requestDTO) {
        userService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FindUserDto> findById(@PathVariable UUID userId){
        User findUser = userService.findById(userId);
        BinaryProfileContent profile = binaryProfileService.findById(userId);
        Boolean isOnline = userStatusService.findById(userId)
                .map(UserStatus::isOnline)
                .orElse(false);
        return ResponseEntity.status(HttpStatus.OK).body(new FindUserDto(findUser, profile.getBytes(), isOnline));
    }

    @GetMapping
    public ResponseEntity<List<FindUserDto>> findAll(){
        Map<User, UserStatus> statusMapByUser = userStatusService.findStatusMapByUserList(userService.findAll());

        List<FindUserDto> userListDTO = new ArrayList<>();
        for (User user : statusMapByUser.keySet()) {
            BinaryProfileContent profile = binaryProfileService.findById(user.getId());
            userListDTO.add(new FindUserDto(user, profile.getBytes(), statusMapByUser.get(user).isOnline()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(userListDTO);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> update(@PathVariable UUID userId, @RequestBody UserDtoForRequest requestDTO){
        userService.update(userId, requestDTO);
        return ResponseEntity.ok("Successfully updated");
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable UUID userId){
        userService.delete(userId);
        return ResponseEntity.ok("Successfully deleted");
    }
}
