package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateDTO;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private static FileChannelService instance;
    private final ChannelRepository channelRepository;



    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;

    }

    public static synchronized FileChannelService getInstance(ChannelRepository channelRepository) {
        if(instance == null){
            instance = new FileChannelService(channelRepository);
        }
        return instance;
    }


    @Override
    public Channel create(User user, String name, String description) {
        if (channelRepository.existsByUser(user)) {
            throw new IllegalArgumentException(user.getEmail() + " not exists");
        }

        Channel newChannel = new Channel(user, name, description, );
        channelRepository.save(newChannel);

        System.out.println("Channel created: " + name + " (설명: " + description + ")");
        return newChannel;
    }

    @Override
    public ChannelResponseDTO find(UUID channelId) {
        try {
            return channelRepository.findAll().stream()
                    .filter(channel -> channel.getOwner().equals(user))
                    .findFirst()
                    .orElseThrow(()-> new IllegalArgumentException((user + " not exists")));

        }catch (IllegalArgumentException e){
            System.out.println("Failed to read channel: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<ChannelResponseDTO> findAllByUserId(UUID userId) {
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
    public ChannelResponseDTO update(ChannelUpdateDTO updateDTO) {
        try{
            Channel newChannel = find(channel.getOwner());
            channel.update(newName, newDescription);
            channelRepository.save(channel);
            System.out.println("Channel updated: " + newName + " (Description: " + newDescription + ")");
            return channel;
        }catch (IllegalArgumentException e){
            System.out.println("Failed to update channel: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(Channel channel) {
        try {
            if(!channelRepository.existsByUser(channel.getOwner())) {
                throw new IllegalArgumentException((channel.getName() + " not exists"));
            }
            System.out.println("Channel deleted: " + find(channel.getOwner()).getName());
            channelRepository.deleteByChannel(channel);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to delete channel: " + e.getMessage());
        }
    }

    @Override
    public ChannelResponseDTO createPrivateChannel(PrivateChannelCreateDTO requestDTO) {
        return null;
    }

    @Override
    public ChannelResponseDTO createPublicChannel(PublicChannelCreateDTO requestDTO) {
        return null;
    }

}
