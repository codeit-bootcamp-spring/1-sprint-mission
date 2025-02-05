package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(User user, String name, String description) {
        try {
            Channel newChannel = new Channel(user, name, description);

//            System.out.println("Checking if user exists: " + user.getEmail()); // 테스트 부분
            if(channelRepository.existsByUser(user)) {
                throw new IllegalArgumentException((user.getEmail() + " not exists"));
            }
            channelRepository.save(newChannel);
            System.out.println("Channel created: " + name + " (설명: " + description + ")");
            return newChannel;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Channel find(User user) {
        try {
            return channelRepository.findAll().stream()
                    .filter(channel -> channel.getUser().equals(user))
                    .findFirst()
                    .orElseThrow(()-> new IllegalArgumentException((user + " not exists")));

        }catch (IllegalArgumentException e){
            System.out.println("Failed to read channel: " + e.getMessage());
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
    public Channel update(Channel channel, String newName, String newDescription) {
        try{
            Channel newChannel = find(channel.getUser());
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
            if(!channelRepository.existsByUser(channel.getUser())) {
                throw new IllegalArgumentException((channel.getName() + " not exists"));
            }
            System.out.println("Channel deleted: " + find(channel.getUser()).getName());
            channelRepository.deleteByChannel(channel);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to delete channel: " + e.getMessage());
        }
    }
}
