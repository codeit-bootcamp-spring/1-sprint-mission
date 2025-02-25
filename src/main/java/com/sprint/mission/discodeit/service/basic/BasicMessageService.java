package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.aspect.UpdateReadStatus;
import com.sprint.mission.discodeit.aspect.UpdateUserStatus;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusService userStatusService;

    @UpdateUserStatus
    @Override
    public Message create(UUID writerID, CreateMessageRequest request) {
        if (request.content().isEmpty()) {
            throw new ServiceException(ErrorCode.EMPTY_CONTENT);
        }

        // 작성자와 채널에 대한 검증
        validUser(writerID);
        validChannel(request.channelID());

        // 해당 첨부자료가 레포지토리에 저장되어 있는지 확인
        if (request.attachmentsID() != null) {
            for (UUID attachmentID : request.attachmentsID()) {
                Optional<BinaryContent> attachment = binaryContentRepository.findById(attachmentID);
                if (attachment.isEmpty()) {
                    throw new ServiceException(ErrorCode.CANNOT_FOUND_ATTACHMENT);
                }
            }
        }

        Message message = new Message(request.content(), writerID, request.channelID(), request.attachmentsID());
        messageRepository.save(message);
        return message;
    }

    @UpdateReadStatus
    @Override
    public List<Message> findAllByChannelId(UUID channelID) {
        validChannel(channelID);

        return messageRepository.findByChannelId(channelID);
    }

    @Override
    public List<Message> getAllMessage() {
        return messageRepository.findAll();
    }

    @UpdateUserStatus
    @Override
    public Message updateMessageContent(UUID writerID, UpdateMessageRequest request) {
        validMessage(request.messageID());
        validUser(writerID);

        Message message = messageRepository.findById(request.messageID())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));

        // 메시지 수정은 작성자만이 할 수 있다.
        if (!message.getWriterID().equals(writerID)) {
            throw new ServiceException(ErrorCode.MESSAGE_EDIT_NOT_ALLOWED);
        }

        message.update(request.newContent(), request.newAttachment());

        return messageRepository.save(message);
    }


    @Override
    public Message deleteMessage(UUID messageID) {
        Message deleteMessage = messageRepository.findById(messageID).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));

        List<UUID> attachmentsID = deleteMessage.getAttachmentsID();
        if (attachmentsID != null && !attachmentsID.isEmpty()) {
            attachmentsID.forEach(id -> {
                binaryContentRepository.findById(id)
                        .ifPresent(binaryContent -> binaryContentRepository.deleteById(id));
            });
        }

        Message delete = messageRepository.delete(deleteMessage);
        return delete;
    }

    private void validUser(UUID userId) {
        if (!userRepository.userExistById(userId)) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
        }
    }

    private void validChannel(UUID channelId) {
        if (!channelRepository.channelExistById(channelId)) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL);
        }
    }

    private void validMessage(UUID messageId) {
        messageRepository.findById(messageId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));
    }
}