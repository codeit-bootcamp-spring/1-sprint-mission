package com.sprint.mission.controller;

import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.dto.response.FindMessageDto;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.service.jcf.addOn.BinaryService;
import com.sprint.mission.service.jcf.main.JCFChannelService;
import com.sprint.mission.service.jcf.main.JCFMessageService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

  private final JCFMessageService messageService;

  @RequestMapping(path = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> save(@RequestPart("dto") MessageDtoForCreate requestDTO,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

    Optional<List<BinaryContentDto>> binaryContentDtoList =  attachments == null || attachments.isEmpty()
        ? Optional.empty()
        : Optional.of(attachments.stream()
            .map(BinaryContentDto::fileToBinaryContentDto)
            .flatMap(Optional::stream)
            .toList());

    messageService.create(requestDTO, binaryContentDtoList);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("M created successfully");
  }

  // @GetMapping("/messages}")
  @RequestMapping("findInChannel")
  public ResponseEntity<List<FindMessageDto>> findInChannel(
      @RequestParam("channelId") UUID channelId) {
    List<Message> messageList = messageService.findAllByChannelId(channelId);
    List<FindMessageDto> dtoList = messageList.stream()
        .map(FindMessageDto::fromEntity).toList();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(dtoList);
  }

  @RequestMapping("message/{id}")
  public ResponseEntity<String> update(@PathVariable("id") UUID messageId,
      @RequestBody MessageDtoForUpdate requestDTO) {
    messageService.update(messageId, requestDTO);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body("Successfully updated");
  }


  @DeleteMapping("messages/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.ok("Successfully deleted");
  }
}
