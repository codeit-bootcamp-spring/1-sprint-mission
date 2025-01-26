package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() { this.data = new HashMap<>(); }

    @Override
    public boolean save(Message message) {
        data.put(message.getId(), message);
        return true;
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Message> readAll(UUID id){
        List<Message> list =  data.values().stream()
                                        .filter(message -> message.getChannel() != null && message.getChannel().getId().equals(id))
                                        .collect(Collectors.toList());

        return list;
    }

    @Override
    public Message modify(UUID id, Message modifiedMessage) {
        return data.replace(id, modifiedMessage);
    }

    @Override
    public boolean deleteById(UUID id) {
        try {
            String sender = data.get(id).getSender().getUsername();
            String content = data.get(id).getContent();
            data.remove(id);
            System.out.println(sender + "님의 메시지(" +content +")를 삭제되었습니다.");
            return true;
        } catch (NullPointerException e){
            System.out.println("유효하지 않은 메시지입니다.\n" + e);
        }
        return false;
    }
}
