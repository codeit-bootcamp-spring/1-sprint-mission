//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//
//import java.io.*;
//import java.util.*;
//
//public class FileChannelService implements ChannelService {
//    private final FileChannelRepository fileChannelRepository;
//
//    public FileChannelService(FileChannelRepository fileChannelRepository) {
//        this.fileChannelRepository = fileChannelRepository;
//    }
//
//
//    @Override
//    public Channel create(Channel.ChannelType channelType, String channelName, String description) {
//        Channel channel = new Channel(channelType, channelName, description);
//        return fileChannelRepository.create(channel);
//    }
//
//    @Override
//    public Channel findById(UUID channelId) {
//        return fileChannelRepository.findById(channelId)
//                .orElseThrow(()-> new NoSuchElementException("Channel with id "+channelId+" not found"));
//    }
//
//    @Override
//    public List<Channel> findAll() {
//        return fileChannelRepository.findAll();
//    }
//
//    @Override
//    public Channel update(UUID channelId, String field, Object value) {
//        Optional<Channel> optionalChannel = fileChannelRepository.findById(channelId);
//        if (optionalChannel.isPresent()) {
//            Channel channel = optionalChannel.get();
//            switch (field) {
//                case "channelName":
//                    channel.setChannelName((String) value);
//                    break;
//                case "description":
//                    channel.setDescription((String) value);
//                    break;
//                default:
//                    throw new IllegalArgumentException("Invalid field: " + field);
//            }
//            return fileChannelRepository.create(channel);
//        }
//        return null;
//    }
//
//
//    @Override
//    public void delete(UUID channelId) {
//        fileChannelRepository.delete(channelId);
//    }
//}
