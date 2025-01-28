package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.ChannelValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private static final Path filePath = Paths.get(System.getProperty("user.dir"), "tmp/channel.ser");
    private final ChannelValidator channelValidator = new ChannelValidator();
    private final UserService userService;

    public FileChannelService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Channel createChannel(ChannelType channelType, String title, String description) {
        if (channelValidator.isValidTitle(title)) {
            Channel newChannel = new Channel(channelType, title, description);
            List<Channel> saveChannelList = loadChannelListToFile();
            saveChannelList.add(newChannel);
            saveChannelToFile(saveChannelList);
            System.out.println("create new channel: " + newChannel.getTitle());
            return newChannel;
        }
        return null;
    }

    @Override
    public List<Channel> getAllChannelList() {
        return loadChannelListToFile();
    }

    @Override
    public Channel searchById(UUID channelId) {
        for (Channel channel : loadChannelListToFile()) {
            if (channel.getId().equals(channelId)) {
                return channel;
            }
        }
        System.out.println("Channel does not exist");
        return null;
    }

    @Override
    public void updateTitle(UUID channelId, String title) {
        if (!Objects.isNull(searchById(channelId)) && channelValidator.isValidTitle(title)) {
            List<Channel> channelList = loadChannelListToFile();
            for (Channel channel : channelList) {
                if (channel.getId().equals(channelId)) {
                    channel.updateTitle(title);
                }
            }
            saveChannelToFile(channelList);
        }
    }

    @Override
    public void updateDescription(UUID channelId, String description) {
        if (!Objects.isNull(searchById(channelId))) {
            List<Channel> channelList = loadChannelListToFile();
            for (Channel channel : channelList) {
                if (channel.getId().equals(channelId)) {
                    channel.updateDescription(description);
                }
            }
            saveChannelToFile(channelList);
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (!Objects.isNull(searchById(channelId))) {
            List<Channel> saveChannelList = new ArrayList<>();
            for (Channel channel : loadChannelListToFile()) {
                if(!channel.getId().equals(channelId)) {
                    saveChannelList.add(channel);
                }
            }
            saveChannelToFile(saveChannelList);
        }
    }

    @Override
    public void addMember(UUID channelId, UUID userId) {
        Channel channel = searchById(channelId);
        User user = userService.searchById(userId);
        if (!Objects.isNull(user) && !Objects.isNull(channel)) {
            if (channel.getMemberList().contains(user)) {
                System.out.println("user's already a channel member");
            } else {
                List<Channel> channelList = loadChannelListToFile();
                channel.addMember(user);
                saveChannelToFile(channelList);
                // 유저 채널리스트에도 추가
            }
        }
    }

    @Override
    public void deleteMember(UUID channelId, UUID userId) {
        User user = userService.searchById(userId);
        Channel channel = searchById(channelId);
        if (!Objects.isNull(user) && !Objects.isNull(channel)) {
            if (channel.getMemberList().contains(user)) {
                List<Channel> channelList = loadChannelListToFile();
                channel.removeMember(user);
                saveChannelToFile(channelList);
                System.out.println("success delete");
            } else {
                System.out.println("member does not exist");
            }
        }
    }

    @Override
    public List<User> getAllMemberList(UUID channelId) {
        Channel channel = searchById(channelId);
        if (!Objects.isNull(channel)) {
            return channel.getMemberList();
        }
        return null;
    }

    private void saveChannelToFile(List<Channel> saveChannelList) {
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            for (Channel channel : saveChannelList) {
                oos.writeObject(channel);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Channel> loadChannelListToFile() {
        List<Channel> data = new ArrayList<>();
        if(!Files.exists(filePath)) {
            return data;
        }
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                data.add((Channel) ois.readObject());
            }
        } catch (EOFException e) {
//            System.out.println("read all objects");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
