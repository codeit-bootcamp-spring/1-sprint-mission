package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private static JCFMessageService instance; // 싱글톤 인스턴스
    private final Map<UUID, Message> data = new HashMap<>(); // 메시지 데이터 저장소
    private final UserService userService; // 사용자 서비스
    private final ChannelService channelService; // 채널 서비스

    // private 생성자: 싱글톤 패턴
    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    // 싱글톤 인스턴스 제공 메서드
    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if (instance == null) {
            synchronized (JCFMessageService.class) {
                if (instance == null) {
                    instance = new JCFMessageService(userService, channelService);
                }
            }
        }
        return instance;
    }

    @Override
    public void create(Message message) {
        // 발신자와 채널의 유효성을 확인
        if (userService.read(message.getSenderId()).isEmpty()) {
            throw new IllegalArgumentException("User not found: " + message.getSenderId());
        }
        if (channelService.read(message.getChannelId()).isEmpty()) {
            throw new IllegalArgumentException("Channel not found: " + message.getChannelId());
        }

        // 메시지를 데이터 저장소에 추가
        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id)); // 특정 메시지를 조회
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values()); // 모든 메시지를 리스트로 반환
    }

    @Override
    public void update(UUID id, Message message) {
        // 메시지가 존재하면 업데이트
        if (data.containsKey(id)) {
            data.put(id, message);
        } else {
            throw new IllegalArgumentException("Message not found for ID: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        // 메시지를 삭제
        data.remove(id);
    }
}
