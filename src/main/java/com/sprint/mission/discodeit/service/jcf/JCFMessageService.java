package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.exception.validation.message.InvalidMessageException;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        data = new HashMap<>();
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {
        if (content == null || content.trim().isEmpty()) {
            throw new InvalidMessageException("유효하지 않은 문장입니다.");
        }
        Message message = new Message(content, authorId, channelId);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID contentId) {
        return data.get(contentId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID contentId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new InvalidMessageException("유효하지 않은 문장입니다.");
        }
        Message checkMessage = data.get(contentId);
        checkMessage.update(content);
        return checkMessage;
    }

    @Override
    public void delete(UUID contentId) {
        if (!data.containsKey(contentId)) {
            throw new NotfoundIdException("유효하지 않은 Id입니다.");
        }
        data.remove(contentId);
    }
}
