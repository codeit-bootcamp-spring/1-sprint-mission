package com.sprint.mission.discodeit.service.jcf;



import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;


public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private static JCFChannelService instance; // 싱글톤 인스턴스
    private final UserService userService; // 의존성 주입

    private JCFChannelService(ChannelRepository channelRepository,UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    public static synchronized JCFChannelService getInstance(ChannelRepository channelRepository, UserService userService) {
        if (instance == null) {
            instance = new JCFChannelService(channelRepository, userService);
        }
        return instance;
    }

    @Override
    public void createChannel(Channel channel) {
        if(userService.readUser(channel.getUserId()) == null){
            System.out.println("Cannot create channel. User with ID" + channel.getUserId() + " does not exist.");
            return;
        }
        List<Channel>existingChannels = channelRepository.findAll();
        for(Channel existingChannel : existingChannels){
            if(existingChannel.getChannelTitle().equals(channel.getChannelTitle())){
                System.out.println("Channel title '" + channel.getChannelTitle() +"' already exists.");
            }
        }
        channelRepository.save(channel);
        System.out.println("Channel created: " + channel.getChannelTitle() + " (UUID: " + channel.getChannelUuid() + ")");

    }
    @Override
    public Channel readChannel(String channelUuid) {
        return channelRepository.findByUuid(channelUuid);
    }

    @Override
    public List<Channel> readAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(String channelUuid, String newChannelTitle) {
        Channel channel = channelRepository.findByUuid(channelUuid);
        if(channel != null){
            channel.setChannelTitle(newChannelTitle);
            System.out.println("Channel updated: " + channel.getChannelTitle());
        }else{
            System.out.println("Channel with UUID " + channelUuid + "not found.");
        }
    }

    @Override
    public void deleteChannel(String channelUuid) {
        channelRepository.delete(channelUuid);
        System.out.println("Channel deleted: " + channelUuid);
    }
}
