package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicMessageService(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Message create(String content, User writer, Channel channel) {
        if (content.isEmpty()) {
            throw new ServiceException(ErrorCode.EMPTY_CONTENT);
        }

        existUser(writer);
        existChannel(channel);
        Message message = new Message(content, writer, channel);
        messageRepository.save(message);
        return message;
    }

    @Override
    public List<Message> getMessageByUser(User writer) {
        existUser(writer);
        return messageRepository.findByUser(writer);
    }

    @Override
    public List<Message> getMessageByChannel(Channel channel) {
        existChannel(channel);
        return messageRepository.findByChannel(channel);
    }

    @Override
    public Message updateMessageContent(Message message, String newContent) {
        messageRepository.findById(message.getId())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));

        message.update(newContent);

        messageRepository.save(message);
        return message;
    }

    @Override
    public void deleteMessageByWriter(User writer, UUID uuid) {
        existUser(writer);
        Message removeMessage = messageRepository.findById(uuid)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));
        messageRepository.delete(removeMessage);
    }

    @Override
    public void deleteMessageByChannel(Channel channel, UUID uuid) {
        existChannel(channel);
        Message removeMessage = messageRepository.findById(uuid)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));
        messageRepository.delete(removeMessage);

    }

    private void existUser(User user) {
        userRepository.findByPhone(user.getPhone())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));
    }

    private void existChannel(Channel channel) {
        channelRepository.findByName(channel.getName())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));
    }
}