package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private static FileChannelService instance;
    private final UserService userService;
    private final ChannelRepository channelRepository;


    public FileChannelService(ChannelRepository channelRepository, UserService userService) {
        this.userService = userService;
        this.channelRepository = channelRepository;
    }

    public static synchronized FileChannelService getInstance(ChannelRepository channelRepository, UserService userService){
        if(instance == null){
            instance = new FileChannelService(channelRepository,userService);
        }
        return instance;
    }


    @Override
    public void createChannel(Channel channel) {
        if (userService.readUser(channel.getUserId()) == null) {
            return;
        }
        try {
            channelRepository.save(channel);
            System.out.println("Channel created: " + channel.getChannelTitle() + " (User ID: " + channel.getUserId() + ")");
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Create Channel >>>> " + e.getMessage());
        }
    }

    @Override
    public Channel readChannel(String channelUuid) {
        try {
            return channelRepository.findByUuid(channelUuid);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Search Channel >>>> " + e.getMessage());
            return null;
        }

    }

    @Override
    public List<Channel> readAllChannels() {
        List<Channel> channels = channelRepository.findAll();
        if(channels.isEmpty()){
            System.out.println("   No Channel Found ");
        }
        return channels;
    }

    @Override
    public void updateChannel(String channelUuid, String newChannelTitle) {
        try {
            Channel channel = channelRepository.findByUuid(channelUuid);
            channel.setChannelTitle(newChannelTitle);
            channelRepository.delete(channelUuid);
            channelRepository.save(channel);
            System.out.println("Channel updated: " + channel.getChannelTitle() + " (User ID: " + channel.getUserId() + ")");
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Update Channel >>>> " + e.getMessage());
        }
    }

    @Override
    public void deleteChannel(String channelUuid) {
       try {
           Channel channel = channelRepository.findByUuid(channelUuid);
           if (channel == null) {
               throw new IllegalArgumentException("Channel with UUID " + channelUuid + " not found.");
           }
           System.out.println("Channel : " + channelRepository.findByUuid(channelUuid).getChannelTitle() + " deleted.");
           channelRepository.delete(channelUuid);
       }catch (IllegalArgumentException e){
           System.out.println("Failed to Delete Channel >>>> " + e.getMessage());
       }

    }

}
