package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    List<Message> data = new ArrayList<>();

    @Override
    public void createCommonMessage(User sender, String content) {
        Message message = Message.ofCommon(sender, content);
        data.add(message);
    }

    @Override
    public void createReplyMessage(User sender, String content) {
        Message message = Message.ofReply(sender, content);
        data.add(message);
    }

    @Override
    public Optional<Message> findMessage(UUID id) {
        Optional<Message> message = data.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst();
        return message;
    }

    @Override
    public List<Message> findAllMessages() {
        return data;
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        findMessage(id).ifPresentOrElse(m -> m.updateContent(newContent), () -> {
            throw new IllegalArgumentException("message not found: " + id);
        });
    }

    @Override
    public void removeMessage(UUID id) {
        findMessage(id).ifPresent(data::remove);
    }
}
