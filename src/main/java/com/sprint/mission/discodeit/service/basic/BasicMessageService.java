package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(BasicMessageService.class);

    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;


    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        ChannelResponse channel = channelService.getChannelById(channelId);

        if (channel == null) {
            logger.error("Channel을 찾을 수 없습니다!");
            return null;
        }

        UserResponse user = userService.getUserById(userId);
        if (user == null) {
            logger.error("유저를 찾을 수 없습니다");
            return null;
        }

        Message message = new Message(userId, channelId, content);
        messageRepository.save(message);
        logger.info("메시지 생성 성공");
        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        return messageRepository.findById(messageId);
    }


    @Override
    public List<Message> getMessagesByChannel(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public List<Message> getMessagesBySender(UUID senderId) {
        return messageRepository.findAllBySenderId(senderId);
    }

    @Override
    public Message updateMessageContent(UUID messageId, String newContent) {
        Message message = getMessageById(messageId);
        if (message != null) {
            message.updateContent(newContent);
            messageRepository.save(message);
            logger.info("메시지 수정 완료");
        } else {
            logger.error("메시지 수정 실패: 존재하지 않는 messageId");
        }
        return message;
    }


    @Override
    public boolean deleteMessage(UUID messageId) {
        Message message = getMessageById(messageId);
        if (message != null) {
            messageRepository.deleteById(messageId);
            logger.info("메시지 삭제 완료");
            return true;
        } else {
            logger.error("메시지 삭제 실패: 존재하지 않는 messageId");
        }
        return false;
    }
}