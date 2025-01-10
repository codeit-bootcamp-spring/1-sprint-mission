package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<String, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    // 의존성 주입
    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void create(Message message) {
        // 검증 로직: User와 Channel이 존재하는지 확인
        if (!userService.findById(message.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User does not exist: " + message.getUserId());
        }
        if (!channelService.findById(message.getChannelId()).isPresent()) {
            throw new IllegalArgumentException("Channel does not exist: " + message.getChannelId());
        }

        // 데이터 저장
        data.put(message.getId().toString(), message);
    }

    @Override
    public Optional<Message> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(String id, String newContent) {
        Message message = data.get(id);
        if (message != null) {
            message.updateContent(newContent);
        }
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
