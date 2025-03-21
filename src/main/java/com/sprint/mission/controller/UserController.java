package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.dto.response.SaveUserDto;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.UserService;
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

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(path = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> create(@RequestPart("createRequestDto") UserDtoForCreate requestDTO,
                                                 @RequestPart(value = "profile", required = false) MultipartFile profile) {
        Optional<BinaryContentDto> binaryContentDto = BinaryContentDto.fileToBinaryContentDto(profile);
        User user = userService.create(requestDTO, binaryContentDto);
        return CommonResponse.toResponseEntity
                (CREATED, "유저가 성공적으로 생성되었습니다.", SaveUserDto.fromEntity(user));
    }

    @RequestMapping(path = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> update(
            @RequestParam("userId") UUID userId,
            @RequestPart("updateRequestDto") UserDtoForUpdate requestDTO) {

        userService.update(userId, requestDTO);
        return CommonResponse.toResponseEntity
                (OK, "성공적으로 업데이트되었습니다", requestDTO);
    }


    @RequestMapping("delete")
    public ResponseEntity<CommonResponse> delete(@RequestParam("userId") UUID userId) {
        userService.delete(userId);
        return CommonResponse.toResponseEntity
                (NO_CONTENT, "성공적으로 삭제되었습니다", null);
    }

    @RequestMapping("updateStatusByUserId")
    public ResponseEntity<CommonResponse> updateStatusByUserId(@RequestParam("userId") UUID userId) {
        UserStatus userStatus = userStatusService.updateByUserId(userId);
        return CommonResponse.toResponseEntity
                (OK, "status updated Successfully", userStatus);
    }


    @RequestMapping("findAll")
    public ResponseEntity<CommonResponse> findAll() {
        Map<User, Boolean> statusMapByUser = userStatusService.findStatusMapByUserList();
        log.info("statusMapByUser : {}", statusMapByUser);
        List<FindUserDto> findUserDtos = statusMapByUser.keySet().stream()
                .map(user -> {
                    return FindUserDto.fromEntityAndStatus(user, statusMapByUser.get(user));
                }).toList();

        return CommonResponse.toResponseEntity
                (OK, "유저 리스트 조회 성공", findUserDtos);
    }
}
