package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validation.MessageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMassageService implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageValidator messageValidator;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentService binaryContentService;

    @Override
    public MessageResponse createMessage(MessageRequest.Create request, List<MultipartFile> messageFiles) {
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_FOUND, "userId : " + request.userId()));
        Channel channel = channelRepository.findById(request.channelId()).orElseThrow(() -> new RestApiException(ErrorCode.CHANNEL_NOT_FOUND, "channelId : " + request.channelId()));

        if (messageValidator.inValidContent(request.content())) {
            Message newMessage = Message.createMessage(request.content(), request.channelId(), request.userId());
            messageRepository.save(newMessage);

            if (messageFiles != null) {
                messageFiles.forEach(file -> binaryContentService.createMessageFile(file, newMessage.getId()));
            }
            log.info("Create Message: {}", newMessage);
            return MessageResponse.EntityToDto(newMessage);
        }
        return null;
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(MessageResponse::EntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse findById(UUID id) {
        return MessageResponse.EntityToDto(findByIdOrThrow(id));
    }

    @Override
    public MessageResponse update(UUID id, MessageRequest.Update request, List<MultipartFile> messageFiles) {
        Message message = findByIdOrThrow(id);
        if (messageValidator.inValidContent(request.content())) {
            message.update(request.content());
            messageRepository.save(message);

            if (messageFiles != null) {
                messageFiles.forEach(file -> binaryContentService.createMessageFile(file, id));
            }
            log.info("update message: {}", message);
            return MessageResponse.EntityToDto(message);
        }
        return null;
    }

    @Override
    public void deleteById(UUID id) {
        binaryContentService.deleteAllByMessageId(id);
        messageRepository.deleteById(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        messageRepository.findAllByChannelId(channelId).forEach(message -> deleteById(message.getChannelId()));
    }

    @Override
    public Message findByIdOrThrow(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.MESSAGE_NOT_FOUND, "id : " + id));
    }
}
