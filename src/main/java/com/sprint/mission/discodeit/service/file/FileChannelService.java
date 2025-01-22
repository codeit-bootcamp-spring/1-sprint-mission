package com.sprint.mission.discodeit.service.file;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository fileChannelRepository;

    // mocking 이용으로 추가
    private InputHandler inputHandler;

    public FileChannelService(ChannelRepository fileChannelRepository, InputHandler inputHandler){
        this.fileChannelRepository = fileChannelRepository;
        this.inputHandler = inputHandler;
    }

    public UUID createChannel(User user, String channelName){
        // Channel 생성
        Channel channel = new Channel(user, channelName);
        fileChannelRepository.saveChannel(channel);
        return channel.getId();
    }

    // Read : 전체 채널 조회, 특정 채널 조회
    public int showAllChannels(){
        System.out.println(fileChannelRepository.getAllChannels());
        return fileChannelRepository.getAllChannels().size();
    }

    public Channel getChannelById(UUID id){
        return fileChannelRepository.findChannelById(id).orElse(null);
    }

    // Update : 특정 채널 이름 변경
    public void updateChannelName(UUID id){
        String newNickname = inputHandler.getNewInput();

        Channel channel = getChannelById(id);
        channel.setChannelName(newNickname);

        fileChannelRepository.saveChannel(channel);
    }

    // Delete : 전체 채널 삭제, 특정 채널 삭제
    public void deleteAllChannels(){
        fileChannelRepository.deleteAllChannels();
    }
    public void deleteChannelById(UUID id){
        fileChannelRepository.deleteChannelById(id);
    }
}
