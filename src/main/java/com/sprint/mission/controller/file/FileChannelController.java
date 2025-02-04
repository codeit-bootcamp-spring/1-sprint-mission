package com.sprint.mission.controller.file;

import com.sprint.mission.controller.ChannelController;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.file.FileChannelService;
import com.sprint.mission.service.file.FileMessageService;
import com.sprint.mission.service.file.FileUserService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileChannelController implements ChannelController {

    private final FileChannelService fileChannelService = FileChannelService.getInstance();
    private final FileUserService fileUserService = FileUserService.getInstance();
    private final FileMessageService fileMessageService = FileMessageService.getInstance();

    @Override
    public Channel create(String channelName) {
        try {
            fileChannelService.validateDuplicateName(channelName);
            return fileChannelService.createOrUpdate(new Channel(channelName));
        } catch (IOException e) {
            throw new RuntimeException("채널 등록 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public Channel updateChannelName(UUID channelId, String newName) {
        return fileChannelService.update(fileChannelService.findById(channelId), newName);
    }

    @Override
    public Channel findById(UUID channelId){
        return fileChannelService.findById(channelId);
    }

    @Override
    public Set<Channel> findAll() {
        return fileChannelService.findAll();
    }

    @Override
    public Channel findChannelByName(String channelName) {
        Channel findChannel = fileChannelService.findAll().stream().collect(Collectors.toMap(
                Channel::getName, Function.identity())).get(channelName);

        if (findChannel == null) {
            System.out.printf("%s는 없는 채널명입니다.", channelName);
            System.out.println();
            return null;
        } else {
            return findChannel;
        }
    }

    @Override
    public Set<Channel> findAllChannelByUser(UUID userId) {
        return fileUserService.findById(userId).getChannelsImmutable();
    }

    @Override
    public void delete(UUID channelId) {
        fileChannelService.delete(fileChannelService.findById(channelId));
    }

    @Override
    public void addUserByChannel(UUID channelId, UUID userId) throws IOException {
        Channel channel = fileChannelService.findById(channelId);
        User user = fileUserService.findById(userId);

        channel.addUser(user);
        fileUserService.createOrUpdate(user);
        fileChannelService.createOrUpdate(channel);
    }

    @Override // 강퇴
    public void drops(UUID channel_Id, UUID droppingUser_Id) throws IOException {
        Channel channel = fileChannelService.findById(channel_Id);
        User user = fileUserService.findById(droppingUser_Id);

        channel.removeUser(user);
        fileChannelService.createOrUpdate(channel);
        fileUserService.createOrUpdate(user);
    }

    /**
     * 채널 디렉토리 생성
     */
    @Override
    public void createChannelDirectory() {
        fileChannelService.createChannelDirect();
    }
}
