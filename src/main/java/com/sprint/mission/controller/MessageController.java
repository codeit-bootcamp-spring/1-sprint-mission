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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {


    private final JCFChannelService channelService;
    private final JCFMessageService messageService;
    private final JCFUserService userService;
    private final BinaryService binaryService;

    @PostMapping("messages")
    public ResponseEntity<String> save(@RequestPart("dto") MessageDtoForCreate requestDTO,
                                       @RequestPart("attachments") List<MultipartFile> attachments) {


        Optional<List<BinaryContentDto>> binaryContentDtoList = attachments.isEmpty()
                ? Optional.of(attachments.stream().map(BinaryContentDto::fileToBinaryContentDto).toList())
                : Optional.empty();

        messageService.create(requestDTO, binaryContentDtoList);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("M created successfully");
    }

    // @GetMapping("/messages}") << body에 channelId 껴 넣는 방법
    @GetMapping("channels/{channelId}/messages")
    public ResponseEntity<List<FindMessageDto>> findInChannel(@PathVariable UUID channelId) {
        List<Message> messageList = messageService.findAllByChannelId(channelId);
        List<FindMessageDto> dtoList = messageList.stream()
            .map(FindMessageDto::fromEntity).toList();

        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @PatchMapping("messages/{id}")
    public ResponseEntity<String> update(@PathVariable("id") UUID messageId,
                                         @RequestBody MessageDtoForUpdate requestDTO) {
        messageService.update(messageId, requestDTO);
        return ResponseEntity.ok("Successfully updated");
    }


    @DeleteMapping("messages/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok("Successfully deleted");
    }
}
