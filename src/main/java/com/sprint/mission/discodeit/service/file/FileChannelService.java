package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
public class FileChannelService implements ChannelService {
    private static final Path ROOT_DIR = Paths.get(System.getProperty("user.dir"), "tmp");
    private static final String CHANNEL_FILE = "channel.ser";
    private UserService userService;
    private MessageService messageService;
    private FileStorage<Channel> fileStorage;

    public FileChannelService(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
        this.fileStorage = new SerializableFileStorage<>(Channel.class);
        fileStorage.init(ROOT_DIR);
    }

    @Override
    public void setDependencies(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public Channel createChannel(Channel channel) {
        List<Channel> channels = readAllChannels();
        if (channels.stream().anyMatch(c -> c.getId().equals(channel.getId()))) {
            throw new IllegalArgumentException("Channel ID already exists: " + channel.getId());
        }
        channels.add(channel);
        fileStorage.save(ROOT_DIR.resolve(CHANNEL_FILE), channels);
        System.out.println(channel);
        return channel;
    }

    @Override
    public void addParticipantToChannel(Channel channel, User user) {
        List<Channel> channels = readAllChannels();
        Optional<Channel> existingChannel = channels.stream()
                .filter(c -> c.getId().equals(channel.getId()))
                .findFirst();

        if (existingChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel does not exist: " + channel.getId());
        }

        Optional<User> existUser = userService.readUser(user);
        if (existUser.isEmpty()) {
            throw new IllegalArgumentException("User does not exist: " + user.getUsername());
        }

        Channel foundChannel = existingChannel.get();
        foundChannel.getParticipants().add(existUser.get());
        fileStorage.save(ROOT_DIR.resolve(CHANNEL_FILE), channels);
        System.out.println(foundChannel.getName() + " 채널에 " + user.getUsername() + " 유저 추가 완료\n");
    }

    @Override
    public Optional<Channel> readChannel(Channel channel) {
        return readAllChannels().stream()
                .filter(c -> c.getId().equals(channel.getId()))
                .findFirst();
    }

    @Override
    public List<Channel> readAllChannels() {
        return fileStorage.load(ROOT_DIR);
    }

    @Override
    public Channel updateChannel(Channel existChannel, Channel updateChannel) {
        List<Channel> channels = readAllChannels();
        Optional<Channel> channelToUpdate = channels.stream()
                .filter(c -> c.getId().equals(existChannel.getId()))
                .findFirst();

        if (channelToUpdate.isEmpty()) {
            throw new NoSuchElementException("Channel not found");
        }

        Channel channel = channelToUpdate.get();
        System.out.println("업데이트 이전 채널 = " + channel);

        channel.updateName(updateChannel.getName());
        channel.updateDescription(updateChannel.getDescription());
        channel.updateParticipants(updateChannel.getParticipants());
        channel.updateMessageList(updateChannel.getMessageList());
        channel.updateTime();

        fileStorage.save(ROOT_DIR.resolve(CHANNEL_FILE), channels);
        System.out.println("업데이트 이후 채널 = " + channel);
        return channel;
    }

    @Override
    public boolean deleteChannel(Channel channel) {
        List<Channel> channels = readAllChannels();
        boolean removed = channels.removeIf(c -> c.getId().equals(channel.getId()));

        if (!removed) {
            return false;
        }

        messageService.deleteMessageByChannel(channel);
        fileStorage.save(ROOT_DIR.resolve(CHANNEL_FILE), channels);
        return true;
    }
}
