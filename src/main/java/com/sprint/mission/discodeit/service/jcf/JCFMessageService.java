package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFMessageService implements MessageService {
    private final Map<String, Message> messages = new HashMap<>();
    private final ChannelService channelService;
    private final UserService userService;

    public JCFMessageService(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public void createMessage(Message message) {
        validateMessage(message);
        if (messages.containsKey(message.getId().toString())) {
            throw new IllegalArgumentException("이미 존재하는 메세지입니다. " + message.getId());
        }
        messages.put(message.getId().toString(), message);
        System.out.println("메세지가 생성되었습니다. " + message.getId());
    }

    @Override
    public Message readMessage(String id) {
        Message message = messages.get(id);
        if (message == null) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다. : " + id );
        }
        return message;
    }
    @Override
    public void updateMessage(Message message) {
        validateMessage(message);
        if (!messages.containsKey(message.getId().toString())) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다 : " + message.getId());
        }
        messages.put(message.getId().toString(), message);
        System.out.println("메세지가 업데이트되었습니다. " + message.getId());
    }
    @Override
    public void deleteMessage(String id) {
        if (!messages.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다. " + id);
        }
        messages.remove(id);
        System.out.println("메세지가 삭제되었습니다. " + id);
    }

    @Override
    public List<Message> readAllMessages() {
        return List.of();
    }
    private void validateMessage(Message message) {
        // 채널 유효성 검증
        Channel channel = channelService.readChannel(message.getChannel());
        if (channel == null) {
            throw new IllegalArgumentException("유효하지 않은 채널 ID입니다: " + message.getChannel());
        }

        // 사용자 유효성 검증
        User user = userService.readUser(message.getAuthor());
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다: " + message.getAuthor());
        }
    }

}
