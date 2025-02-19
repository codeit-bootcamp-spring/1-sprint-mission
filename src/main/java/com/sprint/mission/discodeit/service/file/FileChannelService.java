//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.service.ChannelService;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.io.File;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.UUID;
//import java.io.ObjectInputStream;
//import java.io.FileInputStream;
//
///*
//todo
//    1. 예외, 오류 처리 할 것
//    2. 마스터 임포트 해결
//    3. equals 재정의 필요?
// */
//
//public class FileChannelService implements ChannelService {
//    private static final String FILE_PATH = "files/channels.ser";
//    private List<Channel> channels;
//
//    public FileChannelService() {
//        File directory = new File("files");
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//        this.channels = loadFromFile();
//    }
//
//    @Override
//    public Channel createChannel(String channelName, String description) {
//        Channel channel = new Channel(channelName, description);
//        channels.add(channel);
//        saveToFile();
//        return channel;
//    }
//
//    @Override
//    public Channel getChannelById(UUID channelId) {
//        for (Channel channel : channels) {
//            if (channel.getId().equals(channelId)) {
//                return channel;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Channel> getAllChannels() {
//        return new ArrayList<>(channels);
//    }
//
//    @Override
//    public Channel updateChannelName(UUID channelId, String newName) {
//        Channel channel = getChannelById(channelId);
//        if (channel != null) {
//            channel.updateChannelName(newName);
//            saveToFile();
//        }
//        return channel;
//    }
//
//    @Override
//    public Channel updateDescription(UUID channelId, String newDescription) {
//        Channel channel = getChannelById(channelId);
//        if (channel != null) {
//            channel.updateDescription(newDescription);
//            saveToFile();
//        }
//        return channel;
//    }
//
//    @Override
//    public boolean deleteChannel(UUID channelId) {
//        Channel channel = getChannelById(channelId);
//        if (channel != null) {
//            channels.remove(channel);
//            saveToFile();
//            return true;
//        }
//        return false;
//    }
//
//    private void saveToFile() {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
//            oos.writeObject(channels);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<Channel> loadFromFile() {
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
//            return (List<Channel>) ois.readObject();
//        } catch (Exception e) {
//            return new ArrayList<>();
//        }
//    }
//
//}
