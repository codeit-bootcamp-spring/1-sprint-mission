package com.sprint.mission.discodeit.web.message;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.entity.ChannelGroup;
import com.sprint.mission.discodeit.domain.entity.Message;
import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    @PostMapping
    public ResponseEntity<List<Message>> createMessage() {
        User hyun = new User("hyun", "hyun123", "Hyun", "hyun@example.code");
        User yull = new User("yull", "yull123", "Yull", "yull@example.code");
        Channel hiChannel = new Channel("hiChannel", "Hi", ChannelGroup.PUBLIC);
        Channel howChannel = new Channel("How", "How about u?", ChannelGroup.PRIVATE);
        BinaryContent binaryContent = new BinaryContent("testFile.txt", 100L, "text/plain", "this is test".getBytes(StandardCharsets.UTF_8));
        BinaryContent binaryContent2 = new BinaryContent("testFile2.txt", 200L, "text/plain", "this is test2".getBytes(StandardCharsets.UTF_8));
        List<BinaryContent> binaryList = new ArrayList<>();
        binaryList.add(binaryContent);
        binaryList.add(binaryContent2);

        Message howMessage = new Message("Good", yull.getId(), howChannel.getId());
        Message hiMessage = new Message("Hi I'm hyun with file", hyun.getId(), hiChannel.getId());
        messageService.messageSaveWithContents(hiMessage.getSenderId(), hiMessage, binaryList);
        messageService.messageSave(howMessage.getSenderId(), howMessage);
        List<Message> allMessages = messageService.findAllMessages();
        return ResponseEntity.ok(allMessages);
    }

    @PutMapping("{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable UUID id, @RequestBody String messageParam) {
        messageService.updateMessage(id, messageParam);
        Optional<Message> message = messageService.findMessage(id);
        return ResponseEntity.status(HttpStatus.OK).body(message.orElse(null));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Message> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{channelId}")
    public ResponseEntity<Optional<Message>> getMessages(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<BinaryContent> getBinaryMessages(@PathVariable UUID id) {
        return ResponseEntity.ok(binaryContentService.find(id));
    }

    @PostMapping("files")
    public ResponseEntity<List<BinaryContent>> saveFiles(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(ids));
    }
}
