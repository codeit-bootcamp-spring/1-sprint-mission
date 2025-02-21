package com.sprint.mission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.dto.request.UserDtoForRequest;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final JCFUserService userService;
    private final UserStatusService userStatusService;
    private final BinaryProfileService binaryProfileService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    //@ModelAttribute UserDtoForRequest requestDTO << 이거 써도 되지만 PUT, PATCH에서는 못 쓰기에 일관성 있게 RequestPart
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> create(@RequestPart("dto") UserDtoForRequest requestDTO,
                                         @RequestPart("profileImg") MultipartFile profile) {
        requestDTO.setProfileImg(profile);
        userService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindUserDto> findById(@PathVariable("id") UUID userId) {
        User findUser = userService.findById(userId);
        BinaryProfileContent profile = binaryProfileService.findById(userId);
        boolean isOnline = userStatusService.findById(userId).isOnline();
        return ResponseEntity.status(HttpStatus.OK).body(new FindUserDto(findUser, profile.getBytes(), isOnline));
    }

    @GetMapping("/{id}/channels")
    public ResponseEntity<List<FindChannelDto>> findAllByUserId(@PathVariable("id") UUID userId) {
        User user = userService.findById(userId);
        List<FindChannelDto> findChannelDtoList = user.getChannels().stream()
                .map(this::getFindChannelDto).collect(Collectors.toCollection(ArrayList::new));

        return ResponseEntity.status(HttpStatus.OK).body(findChannelDtoList);
    }

    @GetMapping
    public ResponseEntity<List<FindUserDto>> findAll() {
        Map<User, UserStatus> statusMapByUser = userStatusService.findStatusMapByUserList(userService.findAll());

        List<FindUserDto> userListDTO = new ArrayList<>();
        for (User user : statusMapByUser.keySet()) {
            BinaryProfileContent profile = binaryProfileService.findById(user.getId());
            userListDTO.add(new FindUserDto(user, profile.getBytes(), statusMapByUser.get(user).isOnline()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(userListDTO);
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> update(@PathVariable("id") UUID userId,
                                         @RequestPart("dto") UserDtoForRequest requestDTO,
                                         @RequestPart("profileImg") MultipartFile changedImage) {
        requestDTO.setProfileImg(changedImage);
        // 수정할 것
        userService.update(userId, requestDTO);
        return ResponseEntity.ok("Successfully updated");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok("Successfully deleted");
    }

    //  사용자의 온라인 상태를 업데이트할 수 있다.
    public ResponseEntity<String> updateStatus(@PathVariable("id") UUID userId) {
        userStatusService.update(userId);
        return ResponseEntity.ok("Successfully updated");
    }


    private FindChannelDto getFindChannelDto(Channel findedChannel) {
        return (findedChannel.getChannelType().equals(ChannelType.PRIVATE)
                ? new FindPrivateChannelDto(findedChannel)
                : new FindPublicChannelDto(findedChannel));
    }
}
