package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final MessageValidator messageValidator = new MessageValidator();
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    // 메세지 생성
    @Override
    public Message createMessage(UUID channelId, UUID writerId, String content) {
        Channel getChannel = channelService.searchById(channelId);
        User getWriter = userService.searchById(writerId);

        if (!Objects.isNull(getChannel) && !Objects.isNull(getWriter)) {
            if (messageValidator.inValidContent(content)) {
                Message newMessage = new Message(getChannel, getWriter, content);
                data.put(newMessage.getId(), newMessage);
                System.out.println(getChannel.getTitle() + " channel: " + getWriter.getName() + " send new message");
                return newMessage;
            }
        }
        return null;
    }

    // 메세지 모두 조회
    @Override
    public List<Message> getAllMessageList() {
        return data.values().stream().toList();
    }

    // id로 메세지 조회
    @Override
    public Message searchById(UUID messageId) {
        if (data.containsKey(messageId)) {
            return data.get(messageId);
        } else {
            System.out.println("message dose not exist");
            return null;
        }
    }

    // 메세지 수정
    @Override
    public void updateMessage(UUID messageId, String content) {
        if (data.containsKey(messageId)) {
            if (messageValidator.inValidContent(content)) {
                searchById(messageId).updateContent(content);
                System.out.println("success update");
            }
        }
    }

    // 메세지 삭제
    @Override
    public void deleteMessage(UUID messageId) {
        if (data.containsKey(messageId)) {
            data.remove(messageId);
            System.out.println("success delete");
        }
    }
}
