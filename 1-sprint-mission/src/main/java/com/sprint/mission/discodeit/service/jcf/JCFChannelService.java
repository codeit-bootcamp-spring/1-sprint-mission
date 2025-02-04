package com.sprint.mission.discodeit.service.jcf;



import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;


public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private static JCFChannelService instance;


    private JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;

    }

    public static synchronized JCFChannelService getInstance(ChannelRepository channelRepository) {
        if (instance == null) {
            instance = new JCFChannelService(channelRepository);
        }
        return instance;
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        try {
            Channel channel = new Channel(type, name, description);
            channelRepository.save(channel);
            System.out.println("Channel created: " + name + " (설명: " + description + ")");
            return channel;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Channel find(UUID channelId) {
        try {
            return channelRepository.findById(channelId)
                    .orElseThrow(()-> new IllegalArgumentException("Channel not found"));
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Read Channel >>>> " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Channel> findAll() {
        try {
            List<Channel> channels = channelRepository.findAll();
            if (channels.isEmpty()) {
                throw new IllegalStateException("No channels found in the system.");
            }
            return channels;
        }catch (IllegalStateException e){
            System.out.println("Failed to read all channels: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        try {
            Channel channel = find(channelId);
            channel.update(newName, newDescription);
            channelRepository.save(channel);
            System.out.println("Channel updated: " + newName + " (Description: " + newDescription + ")");
            return channel;
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Update Channel >>>> " + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(UUID channelId) {
        try {
            System.out.println("Channel deleted:  " + find(channelId).getName());
            channelRepository.deleteById(channelId);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to Delete Channel >>>> " + e.getMessage());
        }
    }

}
