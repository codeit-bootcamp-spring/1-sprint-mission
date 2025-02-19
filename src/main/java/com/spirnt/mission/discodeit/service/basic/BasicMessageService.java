package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    @Override
    public Message create(MessageCreateRequest messageCreateRequest) {
        // User와 Channel이 존재하는지 검증
        UUID userId = messageCreateRequest.getUserId();
        UUID channelId=messageCreateRequest.getChannelId();
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel id " + messageCreateRequest.getChannelId() + " does not exist");
        }
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User id " + messageCreateRequest.getUserId() + " does not exist");
        }
        Message message = new Message(messageCreateRequest.getContent(),
                channelId,
                userId);
        messageRepository.save(message);
        // 첨부 파일 업로드
        for (MultipartFile file : messageCreateRequest.getFiles()) {
            BinaryContentCreate binaryContentCreate = new BinaryContentCreate(userId, message.getId(), file);
            binaryContentService.create(binaryContentCreate);
        }
        // 메세지 작성자를 Online 상태로
        userStatusService.updateByUserId(userId, new UserStatusUpdate(UserStatusType.ONLINE, Instant.now()));
        return message;
    }

    @Override
    public MessageResponse find(UUID messageId) {
        Message message =messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " Not Found"));
        return new MessageResponse(message);
    }

    @Override
    public List<MessageResponse> findAll() {
        Map<UUID, Message> data = messageRepository.findAll();
        return data.values().stream()
                .sorted(Comparator.comparing(message -> message.getCreatedAt()))
                .map(message->new MessageResponse(message))
                .collect(Collectors.toList());
    }


    @Override
    public void update(UUID messageId, MessageUpdateRequest dto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " Not Found"));
        message.update(dto.getContent());
        messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (messageRepository.existsById(messageId)) {
            // 첨푸 파일 삭제
            binaryContentService.deleteByMessageId(messageId);
            messageRepository.delete(messageId);
        } else {
            throw new NoSuchElementException("Message ID: " + messageId + "Not Found");
        }

    }

}
