package com.sprint.mission.controller;


//import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.FindChannelAllDto;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.jcf.main.JCFChannelService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("public")
    public ResponseEntity<CommonResponse> create(@RequestBody @Valid PublicChannelCreateDTO request) {
        Channel createdChannel = channelService.createPublicChannel(request);
        return CommonResponse.toResponseEntity
                (CREATED, "Public 채널이 생성되었습니다.", getFindChannelDto(createdChannel));
    }

    @PostMapping("private")
    public ResponseEntity<CommonResponse> create(@RequestBody @Valid PrivateChannelCreateDTO request) {
        Channel createdChannel = channelService.createPrivateChannel(request);
        return CommonResponse.toResponseEntity
                (CREATED, "Private 채널이 생성되었습니다.", getFindChannelDto(createdChannel));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CommonResponse> update(@PathVariable("id") UUID channelId,
                                                 @RequestBody @Valid ChannelDtoForRequest requestDTO) {
        channelService.update(channelId, requestDTO);
        return CommonResponse.toResponseEntity
                (OK, "성공적으로 업데이트되었습니다", requestDTO);
    }

    //[ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
    @GetMapping
    public ResponseEntity<CommonResponse> findAllByUserId(
            @RequestParam("userId") UUID userId) {

        List<FindChannelAllDto> channelDtoList = channelService.findAllByUserId(userId);
        return CommonResponse.toResponseEntity
                (OK, "성공적으로 조회되었습니다", channelDtoList);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse> delete(@PathVariable("id") UUID channelId) {
        channelService.delete(channelId);
        return CommonResponse.toResponseEntity
                (NO_CONTENT, "성공적으로 삭제되었습니다", null);
    }


    /**
     * 응답 DTO (타입별)
     */
    private FindChannelDto getFindChannelDto(Channel findedChannel) {
        return (findedChannel.getChannelType().equals(ChannelType.PRIVATE)
                ? new FindPrivateChannelDto(findedChannel)
                : new FindPublicChannelDto(findedChannel));
    }
}


