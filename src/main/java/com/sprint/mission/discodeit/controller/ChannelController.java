package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelRequestDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {
  //TODO: 컨트롤러단 RequestEntity 로 수정하기.Dto로

  private final ChannelService channelService;

  //공개 채널 생성
  @PostMapping
  public String createPublicChannel(@RequestBody ChannelCreateDTO channelCreateDTO) {
    Channel createdChannel = channelService.createPublicChannel(channelCreateDTO);
    return "Public channel created";
  }

  //비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody PrivateChannelCreateDTO channelCreateDTO) {
    Channel createdChannel = channelService.createPrivateChannel(channelCreateDTO);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  //공개 채널 정보 수정
  @PatchMapping("/{id}")
  public String updateChannel(@PathVariable("id") UUID id,
      @RequestBody ChannelUpdateDTO channelUpdateDTO) {
    channelService.update(channelUpdateDTO);
    return "Channel updated";
  }

  //채널 삭제
  @DeleteMapping("/{id}")
  public String deleteChannel(@PathVariable("id") UUID id) {
    channelService.deleteChannel(id);
    return "Channel deleted";
  }


  //특정 사용자가 볼 수 있는 모든 채널 목록 조회
  @GetMapping("/{userId}")
  public List<ChannelRequestDTO> findAllByUserId(@PathVariable("userId") UUID id) {
    return channelService.findAllByUserId(id);
  }

  @GetMapping
  public List<ChannelRequestDTO> findAll() {
    return channelService.findAllDTO();
  }

}
