package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    private static final Path ROOT_DIR = Paths.get(System.getProperty("user.dir"), "tmp");
    private static final String MESSAGE_FILE = "message.ser";
    private UserService userService;
    private ChannelService channelService;
    private FileStorage<Message> fileStorage;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        this.fileStorage = new SerializableFileStorage<>(Message.class);
        fileStorage.init(ROOT_DIR);
    }

    @Override
    public void setDependencies(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(Message message) {
        if (message.getAuthor() == null || message.getChannel() == null) {
            throw new IllegalArgumentException("Author and Channel cannot be null");
        }

        // 사용자 검증
        Optional<User> existingUser = userService.readUser(message.getAuthor());
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("Author does not exist: " + message.getAuthor().getId());
        }

        // 채널 검증
        Optional<Channel> existingChannel = channelService.readChannel(message.getChannel());
        if (existingChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel does not exist: " + message.getChannel().getId());
        }

        Channel channel = existingChannel.get();

        // 참가자 검증 - ID 기반 비교
        boolean isParticipant = channel.getParticipants().stream()
                .anyMatch(participant -> participant.getId().equals(message.getAuthor().getId()));

        if (!isParticipant) {
            throw new IllegalArgumentException("Author is not a participant of the channel: " + channel.getId());
        }

        List<Message> messages = readAll();
        messages.add(message);
        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
        System.out.println(message + "\n메시지 생성 완료\n");
        return message;
    }

    @Override
    public Optional<Message> readMessage(Message message) {
        return readAll().stream()
                .filter(m -> m.getId().equals(message.getId()))
                .findFirst();
    }

    @Override
    public List<Message> readAll() {
        return fileStorage.load(ROOT_DIR);
    }

    @Override
    public Message updateByAuthor(User user, Message updatedMessage) {
        List<Message> messages = readAll();
        Optional<Message> existingMessage = messages.stream()
                .filter(message -> message.getAuthor().equals(user))
                .findFirst();

        if(existingMessage.isEmpty()) {
            throw new NoSuchElementException("No message found for the given User");
        }

        Message message = existingMessage.get();
        System.out.println("수정 전 메시지 = " + message.getContent());
        message.updateContent(updatedMessage.getContent());
        message.updateChannel(updatedMessage.getChannel());
        message.updateTime();

        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
        System.out.println("수정 후 메시지 = " + message.getContent());
        return message;
    }

    @Override
    public boolean deleteMessage(Message message) {
        List<Message> messages = readAll();
        boolean removed = messages.removeIf(m -> m.getId().equals(message.getId()));

        if (!removed) {
            return false;
        }

        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
        return true;
    }

    @Override
    public void deleteMessageByChannel(Channel channel) {
        List<Message> messages = readAll();
        messages.removeIf(message -> message.getChannel().equals(channel));
        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
    }

    @Override
    public void deleteMessageByUser(User user) {
        List<Message> messages = readAll();
        messages.removeIf(message -> message.getAuthor().equals(user));
        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
    }
}
