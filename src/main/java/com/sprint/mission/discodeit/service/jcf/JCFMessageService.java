package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.common.validation.Validator;
import com.sprint.mission.discodeit.common.validation.ValidatorImpl;
import com.sprint.mission.discodeit.dto.MessageReqDTO;
import com.sprint.mission.discodeit.dto.MessageResDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {

    private final Validator validator = new ValidatorImpl();
    private MessageRepository messageRepository;

    public JCFMessageService() {
        this.messageRepository = new JCFMessageRepository();
    }

    @Override
    public Long createMessage(User author, Channel channel, String content) {
        try {
            if (author == null) {
                throw new CustomException(ErrorCode.AUTHOR_CANNOT_BLANK);
            }
            if (channel == null) {
                throw new CustomException(ErrorCode.CHANNEL_CANNOT_BLANK);
            }
            if (content == null) {
                throw new CustomException(ErrorCode.CONTENT_CANANOT_BLANK);
            }

            Message msg = new Message(new MessageReqDTO(author, channel, content));
            Long msgId = messageRepository.saveMessage(msg);
            return msgId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MessageResDTO getMessage(Long id) {
        Message msg = Objects.requireNonNull(findMessageById(id), "해당 ID의 메시지가 존재하지 않습니다.");
        return new MessageResDTO(id, msg);
    }

    @Override
    public MessageResDTO getMessage(String uuid) {
        Optional<Map.Entry<Long, Message>> msg = Objects.requireNonNull(findMessageByUUID(uuid), "해당 ID의 메시지가 존재하지 않습니다.");
        return new MessageResDTO(msg.get().getKey(), msg.get().getValue());
    }

    @Override
    public Message getMessageToMsgObj(Long id) {
        return Objects.requireNonNull(findMessageById(id), "해당 ID의 메시지가 존재하지 않습니다.");
    }

    @Override
    public List<MessageResDTO> getAllMessage() {
        return messageRepository.loadAllMessages().entrySet().stream()
                .map(entry ->
                        new MessageResDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResDTO> getChannelMessage(String channelName) {
        return messageRepository.loadAllMessages().entrySet().stream()
                .filter(entry -> entry.getValue().getChannel().getId().equals(UUID.fromString(channelName)))
                .map(entry ->
                        new MessageResDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public Message findMessageById(Long id) {
        return messageRepository.loadMessage(id);
    }

    public Optional<Map.Entry<Long, Message>> findMessageByUUID(String uuid) {
        return messageRepository.loadAllMessages().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(UUID.fromString(uuid)))
                .findFirst();
    }

    @Override
    public boolean updateMessage(Long id, MessageUpdateDTO updateInfo) {
        boolean isUpdated = false;
        try {
            Message msg = getMessageToMsgObj(id);
            if (updateInfo.getContent() != null && !msg.getContent().equals(updateInfo.getContent())) {
                msg.updateContent(updateInfo.getContent());
                isUpdated = true;
            }
            messageRepository.updateMessage(id, msg);
            return isUpdated;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MessageResDTO deleteMessage(Long id) {
        MessageResDTO deleteMessage = getMessage(id);
        messageRepository.deleteMessage(id);
        return deleteMessage;
    }

    @Override
    public MessageResDTO deleteMessage(String uuid) {
        MessageResDTO deleteMessage = getMessage(uuid);
        messageRepository.deleteMessage(deleteMessage.getId());
        return deleteMessage;
    }
}
