package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.*;


@Repository
public class FileChannelRepository {

    public void createChannel(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        Channel channel = new Channel();

        if(checkChannelContains(channel.getChannelId())){
            while(checkChannelContains(channel.getChannelId())){
                channel.setChannelId(UUID.randomUUID());
            }
        }

        channel.setName(channelDto.getChannelName());
        channel.setUsers(channelDto.getUserId(),channelDto.getUserName());
        channel.setCheckPrivateChannel(channelDto.getChannelPrivate());

        setChannelListToFile(channel);
    }

    public boolean checkChannelContains(UUID channelId) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();

        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelId)){
                return true;
            }
        }

        return false;
    }

    public void joinChannel(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();
        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelDto.getChannelId())){
                channel.setUsers(channelDto.getUserId(), channelDto.getUserName());
                break;

            }
        }
        modifyChannelOfFile(channels);
    }

    public void deleteChannel(UUID deleteChannelId) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();
        if(channels.isEmpty()){
            System.out.println("없는 서버입니다");
            return;
        }

        for(Channel channel : channels){
            if(channel.getChannelId().equals(deleteChannelId)){
                channels.remove(channel);
                modifyChannelOfFile(channels);
                return;
            }
        }

        System.out.println("없는 서버입니다");
    }

    public void modifyChannelUserName(UUID checkingUserId, String newName) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();

        for(Channel channel : channels){
            if(channel.getUsers().containsKey(checkingUserId)){
                channel.setUsers(checkingUserId,newName);
            }
        }

        modifyChannelOfFile(channels);
    }

    public boolean getChannelId(UUID id) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();

        for(Channel channel : channels){
            if(channel.getChannelId().equals(id)){
                return true;
            }
        }

        return false;
    }

    public void modifyChannel(UUID channelId, String name) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();
        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelId)){
                channel.setName(name);
                channel.setUpdatedAt(Instant.now());
                modifyChannelOfFile(channels);
                break;
            }
        }
        modifyChannelOfFile(channels);
    }

    public Collection<String> joinUserList(UUID channelId) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();
        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelId)){
                return channel.getUsers().values();
            }
        }

        return new ArrayList<>();
    }

    public void deleteUser(UUID id) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();

        for(Channel channel : channels){
            channel.getUsers().remove(id);
        }

        modifyChannelOfFile(channels);
    }

    public Channel getChannel(ChannelDto channel) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();

        for(Channel chan : channels){
            if(channel.getChannelName().equals(chan.getName())){
                return chan;
            }
        }

        return null;
    }

    public List<Channel> findAllChannel() throws IOException, ClassNotFoundException {
        return getChannelListFromFile();
    }

    public List<UUID> joinUserIdList(UUID channelId) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();
        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelId)){
                return channel.getUsers().keySet().stream().toList();
            }
        }

        return new ArrayList<>();
    }

    public List<Channel> findAllByUserId(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        List<Channel> list = new ArrayList<>();
        List<Channel> channels = getChannelListFromFile();

        for(Channel channel : channels){
            if(!channel.getCheckPrivateChannel()) list.add(channel);
            else{
                if(channel.getPrivateUsers().contains(channelDto.getUserId())){
                    list.add(channel);
                }
            }
        }

        return list;
    }

    public Channel findChannel(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();
        for(Channel channel : channels){
            if(channel.getChannelId().equals(channelDto.getChannelId())){
                return channel;
            }
        }

        return null;
    }

    public void checkJoinChannel(UUID checkingUserId, String newName) throws IOException, ClassNotFoundException {
        List<Channel> joinedChannel = new ArrayList<>();
        List<Channel> channels = getChannelListFromFile();

        for(Channel channel : channels){
            if(channel.getUsers().containsKey(checkingUserId)){
                joinedChannel.add(channel);
            }
        }

        modifyChannelUser(joinedChannel, checkingUserId, newName);
    }

    private void modifyChannelUser(List<Channel> joinedChannel, UUID checkUserId, String newName) throws IOException, ClassNotFoundException {
        List<Channel> channels = getChannelListFromFile();

        for(Channel channel : channels){
            if(joinedChannel.contains(channel)){
                channel.getUsers().put(checkUserId,newName);
            }
        }
    }

    private void setChannelListToFile(Channel channel) throws IOException, ClassNotFoundException {
        List<Channel> channels= getChannelListFromFile();
        channels.add(channel);

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Channel.ser"));
            out.writeObject(channels);
            out.close();
        }
        catch (IOException ignored) {}

    }

    private void modifyChannelOfFile(List<Channel> channels) throws IOException {
        try (
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Channel.ser"))) {
            out.writeObject(channels);
        }
    }

    private List<Channel> getChannelListFromFile() throws IOException, ClassNotFoundException {
        List<Channel> channels = new ArrayList<>();

        File file = new File("Channel.ser");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                channels = (List<Channel>) in.readObject();
            }
        }

        return channels;
    }

}
