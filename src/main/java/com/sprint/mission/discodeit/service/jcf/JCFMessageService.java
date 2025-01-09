package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BaseService;

import java.util.*;

public class JCFMessageService implements BaseService<Message> {
    private final Map<UUID, Message> data = new HashMap<>();
    private final JCFUserService userService;  // UserService 의존성 주입

    // 생성자에서 UserService 주입
    public JCFMessageService(JCFUserService userService) {
        this.userService = userService;
    }

    @Override
    public Message create(Message message) {
        User author = message.getAuthor();
        if (message.getContent() == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 문장입니다.");
        }
        if (userService.readById(author.getId()) == null) {
            throw new IllegalArgumentException("사용자가 존재하지않아 메시지를 생성할 수 없습니다.");
        }
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message readById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, Message message) {
        Message checkMessage = data.get(id);
        User author = message.getAuthor();
        if (checkMessage == null  || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 문장입니다.");
        }
        if (userService.readById(author.getId()) == null) {
            throw new IllegalArgumentException("사용자가 존재하지않아 메시지를 생성할 수 없습니다.");
        }
        checkMessage.update(message.getContent());
        return checkMessage;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
