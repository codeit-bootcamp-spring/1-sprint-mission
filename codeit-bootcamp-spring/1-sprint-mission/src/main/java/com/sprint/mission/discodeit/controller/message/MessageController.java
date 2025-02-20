package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> findByAllChannel(
            @PathVariable(name = "channelId") UUID channelId
    ) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    @RequestMapping(
            method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Void> createMessage(
            @RequestPart(name = "messageCreateRequest") MessageCreateRequest messageCreateRequest,
            @RequestPart(name = "binary-content-create-request", required = false) List<MultipartFile> binaryContentCreateRequest
    ) {
        List<BinaryContentCreateRequest> binaryContents = binaryContentCreateRequest.stream()
                .map(file -> {
                    try {
                        return new BinaryContentCreateRequest(
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getBytes()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        messageService.create(messageCreateRequest, binaryContents);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateMessage(
            @PathVariable(name = "messageId") UUID messageId,
            @RequestBody MessageUpdateRequest messageUpdateRequest
    ) {
        messageService.update(messageId, messageUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(
            @PathVariable(name = "messageId") UUID messageId
    ) {
        messageService.delete(messageId);
        return ResponseEntity.ok().build();
    }
}
