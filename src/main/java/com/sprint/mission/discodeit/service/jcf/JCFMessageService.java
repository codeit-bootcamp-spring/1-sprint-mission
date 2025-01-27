package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.messageRepository = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, User writer, Channel channel) throws IllegalArgumentException {
        if (content.isEmpty()) {
            throw new ServiceException(ErrorCode.EMPTY_CONTENT);
        }

        validateUser(writer);

        validateChannel(channel);

        Message message = new Message(content, writer, channel);
        messageRepository.put(message.getId(), message);
        return message;
    }

    // 메시지를 보낸 회원이 메시지 조회하기
    @Override
    public List<Message> getMessageByUser(User writer) {
        validateUser(writer);
        return messageRepository.values().stream()
                .filter(message -> message.getWriter().getPhone().equals(writer.getPhone()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessageByChannel(Channel channel) {
        List<Message> messages = new ArrayList<>();
        return messageRepository.values().stream()
                .filter(message -> message.getChannel().getName().equals(channel.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public Message updateMessageContent(Message findMessage, String newContent) {
        if (newContent.isEmpty()) {
            throw new ServiceException(ErrorCode.EMPTY_CONTENT);
        }
        findMessage.update(newContent);
        return findMessage;
    }

    @Override
    public void deleteMessageByWriter(User writer, UUID uuid) { // 작성자가 작성한 메시지 삭제하기
        if (!messageRepository.containsKey(uuid)) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE);
        }
        validateUser(writer);
        Message findMessage = messageRepository.get(uuid);
        messageRepository.remove(uuid);
    }


    @Override
    public void deleteMessageByChannel(Channel channel, UUID uuid) {
        getMessageByChannel(channel).stream()
                .map(Message::getId)
                .forEach(messageRepository::remove);
    }

    private void validateUser(User user) { // User가 repository에 저장되어 있는지 확인
        User findUser = userService.getUserByPhone(user.getPhone())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));
    }

    private void validateChannel(Channel channel) { // Channel이 repository에 저장되어 있는지 확인
        Channel findChannel = channelService.getChannelByName(channel.getName())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));
    }
}
