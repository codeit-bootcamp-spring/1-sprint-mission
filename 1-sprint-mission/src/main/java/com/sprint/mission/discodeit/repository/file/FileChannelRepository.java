package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileChannelRepository implements ChannelRepository {
    private final static String FILE_PATH = "tmp/channel.ser";

    @Override
    public void save(Channel channel) {
        List<Channel>channelList = loadFromFile();
        //중복 확인
        if(channelList.stream().anyMatch(c -> c.getChannelUuid().equals(channel.getChannelUuid()))){
            throw new IllegalArgumentException("Duplicate Channel UUID: " + channel.getChannelUuid());
        }
        channelList.add(channel);
        saveToFile(channelList);
    }

    @Override
    public Channel findByUuid(String channelUuid) {
        List<Channel> channelList = loadFromFile();
        return channelList.stream()
                .filter(channel -> channel.getChannelUuid().equals(channelUuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Channel with UUID " + channelUuid + " not found."));
    }

    @Override
    public List<Channel> findAll() {
        return loadFromFile();
    }

    @Override
    public void delete(String channelUuid) {
        List<Channel>channelList = loadFromFile();
        boolean removed = channelList.removeIf(channel -> channel.getChannelUuid().equals(channelUuid));
        if(!removed){
            throw new IllegalArgumentException("Channel with UUID " + channelUuid + " not found.");
        }
        saveToFile(channelList);
    }

    private List<Channel>loadFromFile(){
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (List<Channel>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Initializing empty list.");
            return new ArrayList<>();
        }catch (EOFException e){
            System.out.println("EOFException: File is empty or corrupted. Initializing empty list.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void saveToFile(List<Channel>channelList){
        try(ObjectOutputStream oos
                    = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channelList);
        }catch (IOException e){

            throw new RuntimeException("Failed to save channels to file.", e);
        }
    }

}
