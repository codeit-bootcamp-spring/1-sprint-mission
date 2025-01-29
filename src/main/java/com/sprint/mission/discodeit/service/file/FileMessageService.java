package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private static final String FILE_PATH = "messages.dat";

    @Override
    public void createMessage(Message message) {
        List<Message> messages = getAllMessages();
        messages.add(message);
        saveMessages(messages);
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        // UUID로 유저와 채널을 생성 (유저와 채널이 실제로 존재한다고 가정)
        long currentTime = System.currentTimeMillis();
        Channel channel = new Channel(channelId, System.currentTimeMillis(), System.currentTimeMillis(), "ChannelName", "description"); // Dummy 채널
        User user = new User(authorId, currentTime, currentTime, "AuthorName", "email@example.com", "password"); // Dummy 유저

        // 메시지 생성
        UUID messageId = UUID.randomUUID();
        Message message = new Message(messageId, currentTime, currentTime, content, user, channel, authorId, channelId);

        // 메시지를 저장
        createMessage(message);
        return message;
    }
    
    @Override
    public Message getMessage(UUID id) {
        List<Message> messages = getAllMessages();
        for(Message message : messages) {
            if(message.getId().equals(id)) {
                return  message;
            }
        }
        return null;
    }

    @Override
    public List<Message> getAllMessages() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("파일에서 메시지를 읽지 못했습니다.", e);
        }
    }

    @Override
    public void updateMessage(UUID id, String content) {
        List<Message> messages = getAllMessages();
        Message targetMessage = messages.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메시지를 찾을 수 없습니다: " + id));

        targetMessage.update(content); // update 메서드로 updatedAt 갱신 포함
        saveMessages(messages);
    }

    @Override
    public void deleteMessage(UUID id) {
        List<Message> messages = getAllMessages();
        messages.removeIf(message -> message.getId().equals(id));
        saveMessages(messages);
    }

    private void saveMessages(List<Message> messages) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new IllegalStateException("메시지를 파일에 저장하지 못했습니다.", e);
        }
    }

}
