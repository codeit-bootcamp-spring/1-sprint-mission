package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data = new HashMap<>(); // 메시지를 저장하는 필드
    private final UserService userService; // UserService 의존성
    private final ChannelService channelService; // ChannelService 의존성

    // 의존성 주입을 위한 생성자
    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void create(Message message) {
        // User와 Channel의 존재 여부 확인
        if (userService.read(message.getSenderId()).isEmpty()) {
            throw new IllegalArgumentException("User not found: " + message.getSenderId());
        }
        if (channelService.read(message.getChannelId()).isEmpty()) {
            throw new IllegalArgumentException("Channel not found: " + message.getChannelId());
        }

        data.put(message.getId(), message); // 메시지 저장
    }

    @Override
    // 특정 ID로 메시지 조회 -> 데이터 없을시 Optional로 반환
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id)); // 메시지 단건 조회
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values()); // 모든 메시지 조회
    }

    @Override
    public void update(UUID id, Message message) {
        if (data.containsKey(id)) {
            data.put(id, message); // 기존 메시지 수정
        } else {
            throw new IllegalArgumentException("Message not found for ID: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id); // 메시지 삭제
    }
}
