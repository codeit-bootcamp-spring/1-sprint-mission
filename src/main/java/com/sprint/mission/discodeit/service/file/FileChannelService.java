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
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private static final Path ROOT_DIR = Paths.get(System.getProperty("user.dir"), "tmp");
    private static final String CHANNEL_FILE = "channel.ser";
    private UserService userService;
    private FileStorage<Channel> fileStorage;

    public FileChannelService(UserService userService) {
        this.userService = userService;
        this.fileStorage = new SerializableFileStorage<>(Channel.class);
        fileStorage.init(ROOT_DIR);
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
    public void addParticipantToChannel(UUID channelId, UUID userId) {
        List<Channel> channels = readAllChannels();
        Channel existingChannel = channels.stream()
                .filter(c -> c.getId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + channelId));

        User existUser = userService.readUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User does not exist: " + userId));

        existingChannel.getParticipants().put(existUser.getId(), existUser);
        fileStorage.save(ROOT_DIR.resolve(CHANNEL_FILE), channels);
        System.out.println(existingChannel.getName() + " 채널에 " + existUser.getUsername() + " 유저 추가 완료\n");
    }

    @Override
    public Optional<Channel> readChannel(UUID existChannelId) {
        return readAllChannels().stream()
                .filter(c -> c.getId().equals(existChannelId))
                .findFirst();
    }

    @Override
    public List<Channel> readAllChannels() {
        return fileStorage.load(ROOT_DIR);
    }

    @Override
    public Channel updateChannel(UUID existChannelId, Channel updateChannel) {
        List<Channel> channels = readAllChannels();
        Channel existChannel = channels.stream()
                .filter(c -> c.getId().equals(existChannelId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Channel does not exist: " + existChannelId));

        System.out.println("업데이트 이전 채널 = " + existChannel);

        existChannel.updateName(updateChannel.getName());
        existChannel.updateDescription(updateChannel.getDescription());
        existChannel.updateParticipants(updateChannel.getParticipants());
        existChannel.updateMessageList(updateChannel.getMessageList());
        existChannel.updateTime();

        fileStorage.save(ROOT_DIR.resolve(CHANNEL_FILE), channels);
        System.out.println("업데이트 이후 채널 = " + existChannel);
        return existChannel;
    }

    @Override
    public boolean deleteChannel(UUID channelId) {
        List<Channel> channels = readAllChannels();
        boolean removed = channels.removeIf(c -> c.getId().equals(channelId));

        if (!removed) {
            return false;
        }

        fileStorage.save(ROOT_DIR.resolve(CHANNEL_FILE), channels);
        return true;
    }
}
