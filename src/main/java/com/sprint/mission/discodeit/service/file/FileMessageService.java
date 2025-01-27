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
import java.util.UUID;
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
    public Message createMessage(Message message) {
        if (message.getAuthor() == null || message.getChannel() == null) {
            throw new IllegalArgumentException("Author and Channel cannot be null");
        }

        // 사용자 검증
        User existingUser = userService.readUser(message.getAuthor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Author does not exist: " + message.getAuthor().getId()));

        // 채널 검증
        Channel existingChannel = channelService.readChannel(message.getChannel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + message.getChannel().getId()));

        // 참가자 검증 - ID 기반 비교
        if (!existingChannel.getParticipants().containsKey(existingUser.getId())) {
            throw new IllegalArgumentException("Author is not a participant of the channel: "
                    + existingChannel.getId());
        }

        List<Message> messages = readAllMessages();
        messages.add(message);
        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
        System.out.println(message + "\n메시지 생성 완료\n");
        return message;
    }

    @Override
    public Optional<Message> readMessage(UUID existMessageId) {
        return readAllMessages().stream()
                .filter(m -> m.getId().equals(existMessageId))
                .findFirst();
    }

    @Override
    public List<Message> readAllMessages() {
        return fileStorage.load(ROOT_DIR);
    }

    @Override
    public Message updateByAuthor(UUID existUserId, Message updatedMessage) {
        List<Message> messages = readAllMessages();
        Message existingMessage = messages.stream()
                .filter(message -> message.getAuthor().equals(existUserId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No message found for the given user : "+ existUserId));

        System.out.println("수정 전 메시지 = " + existingMessage.getContent());
        existingMessage.updateContent(updatedMessage.getContent());
        existingMessage.updateChannel(updatedMessage.getChannel());
        existingMessage.updateTime();

        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
        System.out.println("수정 후 메시지 = " + existingMessage.getContent());
        return existingMessage;
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        List<Message> messages = readAllMessages();
        boolean removed = messages.removeIf(m -> m.getId().equals(messageId));

        if (!removed) {
            return false;
        }

        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
        return true;
    }
}
