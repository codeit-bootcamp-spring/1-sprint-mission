package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@ResponseBody
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    //메시지 생성
    @RequestMapping(path = "create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(@RequestPart MessageCreateDto messageCreateDto,
                                          @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments){
        List<BinaryContentCreateDto> attachmentList = Optional.ofNullable(attachments)
                .map(files->files.stream()
                        .map(file->{
                            try{
                                return new BinaryContentCreateDto(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                            }catch(IOException e){
                                throw new RuntimeException(e);
                            }
                        })
                        .toList())
                .orElse(new ArrayList<>());
        Message createdMessage = messageService.create(messageCreateDto,attachmentList);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }
    //메시지 수정
    @RequestMapping(path = "update")
    public ResponseEntity<Message> update(@RequestParam("messageId") UUID messageId,
                                          @RequestPart MessageUpdateDto messageUpdateDto
                                          ){
        Message updatedMessage = messageService.update(messageId,messageUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
    }
    //메시지 삭제
    @RequestMapping(path = "delete")
    public ResponseEntity<Void> delete(@RequestParam("messageId") UUID messageId){
        messageService.delete(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //메시지 목록 조회
    @RequestMapping(path = "findAllByChannelId")
    public ResponseEntity<List<Message>> findAllByChannelId(@RequestParam("channelId") UUID channelId){
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

}
