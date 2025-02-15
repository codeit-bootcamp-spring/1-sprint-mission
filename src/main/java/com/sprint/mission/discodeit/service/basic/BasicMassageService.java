package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.MessageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMassageService implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageValidator messageValidator;
    private final UserService userService;
    private final ChannelService channelService;
    private final BinaryContentService binaryContentService;

    @Override
    public Message createMessage(MessageRequest.Create request) {
        Channel channel = channelService.findByIdOrThrow(request.channelId()); // 기존처럼 쓰면 dto로 받아오게 됨. 직접 repository를 써야할까?
        User user = userService.findByIdOrThrow(request.userId());  // 유저가 있는지 확인하기 searchById. 그런데 userService에서 받아와서 쓸지?
        // 서비스 단끼리 통신할 때 dto를 쓸 필요는 없어보임

        if (messageValidator.inValidContent(request.content())) {
            Message newMessage = Message.createMessage(request.content(), request.channelId(), request.userId());
            messageRepository.save(newMessage);

            if (!request.files().isEmpty()) {
                request.files().forEach(binaryContentService::create);
            }

            log.info("Create Message: {}", newMessage);
            return newMessage;
        }

        return null;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public Message searchById(UUID id) {
        return findByIdOrThrow(id);
    }

    @Override
    public void updateMessage(UUID id, MessageRequest.Update request) {
        Message message = findByIdOrThrow(id);
        if (messageValidator.inValidContent(request.content())) {
            message.update(request.content());
            messageRepository.save(message);

            if (!request.files().isEmpty()) {
                request.files().forEach(binaryContentService::create);
                // 기존 파일들 삭제하기
            }
            log.info("update message: {}", message);
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        binaryContentService.deleteAllByMessageId(id);
        messageRepository.deleteById(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        findAllByChannelId(channelId).forEach(message -> deleteMessage(message.getChannelId()));
    }

    @Override
    public Message findByIdOrThrow(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Message does not exist"));
    }

}
