package com.sprint.mission.discodeit.service.jcf;



import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;


public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private static JCFChannelService instance;


    private JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;

    }

    public static synchronized JCFChannelService getInstance(ChannelRepository channelRepository, UserService userService) {
        if (instance == null) {
            instance = new JCFChannelService(channelRepository);
        }
        return instance;
    }

    @Override
    public void createChannel(Channel channel) {
        try {
            channelRepository.save(channel);
            System.out.println("Channel created: " + channel.getChannelTitle() + " (UUID: " + channel.getChannelUuid() + ")");
        }catch (IllegalArgumentException e){
            System.out.println("Cannot found User. User with ID " + channel.getUserId() + " does not exist.");
        }
    }
    @Override
    public Channel readChannel(String channelUuid) {
        try {
            return channelRepository.findByUuid(channelUuid);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Read Channel >>>> " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Channel> readAllChannels() {
        try {
            List<Channel> channels = channelRepository.findAll();
            if (channels.isEmpty()) {
                throw new IllegalStateException("No channels found in the system.");
            }
            return channels;
        }catch (IllegalStateException e){
            System.out.println("Failed to Read All Channels >>>> " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public void updateChannel(String channelUuid, String newChannelTitle) {
        Channel channel = channelRepository.findByUuid(channelUuid);
        try {
            channel.setChannelTitle(newChannelTitle);
            channelRepository.delete(channelUuid);
            channelRepository.save(channel);
            System.out.println("Channel updated: " + channel.getChannelTitle() + " (UUID: " + channelUuid + ")");
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Update Channel >>>> " + e.getMessage());
        }
    }

    @Override
    public void deleteChannel(String channelUuid) {
        try {
            channelRepository.delete(channelUuid);
            System.out.println("Channel deleted: UUID " + channelUuid);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Delete Channel >>>> " + e.getMessage());
        }
    }
}
