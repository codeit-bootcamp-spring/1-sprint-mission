package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<String, Message> data = new HashMap<>();
    private final UserService userService;        // 의존성 추가
    private final ChannelService channelService; // 의존성 추가

    // 생성자에서 의존성 주입
    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(Message message) {
        // 사용자와 채널의 유효성 검증
        if (userService.findById(message.getUserId().toString()) == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + message.getUserId());
        }
        if (channelService.findById(message.getChannelId().toString()) == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + message.getChannelId());
        }

        // 메시지 저장
        data.put(message.getId().toString(), message);
        return message;
    }

    @Override
    public Message findById(String id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(String id, Message message) {
        if (data.containsKey(id)) {
            data.put(id, message);
            return message;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
