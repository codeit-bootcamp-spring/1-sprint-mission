package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.collection.Messages;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final String filePath;
    private final Messages messages;
    private final ChannelService channelService;

    public FileMessageService(String filePath, ChannelService fileChannel) {
        this.filePath = filePath;
        this.channelService = fileChannel;
        this.messages = loadFromFile().orElseGet(Messages::new);
    }

    @Override
    public Optional<Message> createMessage(UUID authorID, UUID channelID, String text) {
        return channelService.getChannel(channelID)
                .map(channel -> {
                    Message newMessage = new Message(text, authorID, channelID);
                    messages.add(newMessage.getId(), newMessage);
                    saveToFile();
                    return newMessage;
                });
    }

    @Override
    public Map<UUID, Message> getMessages() {
        return messages.asReadOnly();
    }

    @Override
    public Optional<Message> getMessage(UUID uuid) {
        return messages.get(uuid);
    }

    @Override
    public Optional<Message> updateMessage(UUID uuid, String text) {
        Optional<Message> updatedMessage = messages.update(uuid, text);
        if (updatedMessage.isPresent()) {
            saveToFile();
        }
        return updatedMessage;
    }

    @Override
    public Optional<Message> deleteMessage(UUID uuid) {
        Optional<Message> removedMessage = messages.remove(uuid);
        if (removedMessage.isPresent()) {
            saveToFile();
        }
        return removedMessage;
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException("메시지를 저장하는데 실패", e);
        }
    }

    private Optional<Messages> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return Optional.of((Messages) ois.readObject());
        } catch (FileNotFoundException e) {
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지를 저장하는데 실패", e);
        }
    }
}
