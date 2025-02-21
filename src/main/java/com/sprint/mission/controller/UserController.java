package com.sprint.mission.controller;

import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.jcf.addOn.BinaryService;
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
@RequestMapping("/api/users")
public class UserController {

    private final JCFUserService userService;
    private final UserStatusService userStatusService;
    private final BinaryService binaryProfileService;

    @PostMapping(MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> create(@RequestPart("dto") UserDtoForCreate requestDTO,
                                         @RequestPart("profile") MultipartFile profile) {

        Optional<BinaryContentDto> binaryContentDto = BinaryContentDto.fileToBinaryContentDto(profile);

        userService.create(requestDTO, binaryContentDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully");
    }



    @PatchMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> update(@PathVariable("id") UUID userId,
                                         @RequestPart("dto") UserDtoForUpdate requestDTO) {

        userService.update(userId, requestDTO);
        return ResponseEntity.ok("Successfully updated");
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }



    @PatchMapping("{id}/status")
    public ResponseEntity<String> updateStatusByUserId(@PathVariable("id") UUID userId) {
        userStatusService.updateByUserId(userId);
        return ResponseEntity.ok("Successfully updated");
    }

//
//    @GetMapping
//    public ResponseEntity<List<FindUserDto>> findAll() {
//        Map<User, UserStatus> statusMapByUser = userStatusService.findStatusMapByUserList(userService.findAll());
//
//        List<FindUserDto> userListDTO = new ArrayList<>();
//        for (User user : statusMapByUser.keySet()) {
//            BinaryProfileContent profile = binaryProfileService.findById(user.getId());
//            userListDTO.add(new FindUserDto(user, profile.getBytes(), statusMapByUser.get(user).isOnline()));
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(userListDTO);
//    }

}
