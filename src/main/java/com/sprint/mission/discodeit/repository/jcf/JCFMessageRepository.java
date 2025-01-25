package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {
    private List<Message> data;
    public JCFMessageRepository() {
        data = new ArrayList<Message>();
    }
    // 저장
    public boolean saveMessage(Message message) {
        try {
            if (message == null) {
                throw new IllegalArgumentException("message is null");
            }
            if (data.contains(message)) {
                deleteMessage(message);
            }
            data.add(message);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // 조회
    public Message loadMessage(Message message) {
        return data.stream().filter(d -> d.getId().equals(message.getId()))
                .findFirst().orElse(null);
    }
    public List<Message> getMessagesByChannel(Message message) {
        return data.stream().filter(d -> d.getChannel().getChannelName().equals(message.getChannel().getChannelName()))
                .collect(Collectors.toList());
    }
    public List<Message> loadAllMessages() {
        return Collections.unmodifiableList(data);
    }

    // 삭제
    public boolean deleteMessage(Message message) {
        try {
            if (!data.contains(message)) {
                throw new IllegalArgumentException("Message not found");
            }
            if (data.remove(message)) {
                return true;
            }
            throw new RuntimeException("Failed to remove message");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
