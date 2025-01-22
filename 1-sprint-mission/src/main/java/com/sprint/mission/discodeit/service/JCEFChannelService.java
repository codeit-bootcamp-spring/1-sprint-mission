package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.*;

public class JCEFChannelService implements ChannelService{

    private final Map<UUID,Channel> channelList=new TreeMap<>();

    @Override
    public void CreateChannelDefault(String name) {
        if(find_Channel(name).isEmpty()){
            Channel new_channel=Channel.CreateDefaultChannel(name);
            channelList.put(new_channel.getId(),new_channel);
        }else {
            System.out.println("이미 존재하는 채널입니다.");
        }

    }


    @Override
    public <T>String ReadChannel(T key) {
        StringBuilder list= new StringBuilder();
        TreeMap<UUID,Channel> foundChannel=new TreeMap<>();
        if (key instanceof String) {
            foundChannel = find_Channel((String) key);
        } else if (key instanceof UUID) {
            foundChannel = find_Channel((UUID) key);
        }

        for(Channel val:foundChannel.values()){
            list.append(val.toString());
        }

        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 채널이 없습니다.";
    }

    @Override
    public String ReadChannelAll() {
        StringBuilder list= new StringBuilder();
        for(Channel val:channelList.values()){
            list.append(val.toString());
        }

        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "등록된 채널이 없습니다.";
    }

    @Override
    public boolean UpdateChannelName(UUID id, String name) {
        TreeMap<UUID,Channel> instance = find_Channel(id);
        return updateChannel(instance,name);
    }
    public boolean UpdateChannelName(String ChannelName, String name) {
        TreeMap<UUID,Channel> instance = find_Channel(ChannelName);
        return updateChannel(instance,name);
    }
    @Override
    public boolean DeleteChannel(UUID id) {
        TreeMap<UUID,Channel> instance = find_Channel(id);
        return deleteChannel(instance);
    }

    @Override
    public boolean DeleteChannel(String Name) {
        TreeMap<UUID,Channel> instance = find_Channel(Name);
        return deleteChannel(instance);
    }


    private TreeMap<UUID,Channel> find_Channel(String name) {
        TreeMap<UUID,Channel> findChannel=new TreeMap<>();
        for(Channel channel:channelList.values()){
            if(channel.getChannelName().equals(name)){
                findChannel.put(channel.getId(),channel);
            }
        }
        return findChannel;
    }


    private TreeMap<UUID,Channel> find_Channel(UUID ID) {
        TreeMap<UUID,Channel> findChannel=new TreeMap<>();
        findChannel.put(ID,channelList.get(ID));
        return findChannel;
    }


    private boolean deleteChannel(TreeMap<UUID,Channel> instance) {
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 채널이 없습니다.");
            return false;
        }else if(instance.size()==1){
            Channel find=instance.firstEntry().getValue();
            find.deleteExistChannelId();
            channelList.remove(find.getId());
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        }else{
            System.out.println("중복된 채널이 있습니다.");
            return false;
        }
    }

    private boolean updateChannel(TreeMap<UUID,Channel> instance, String changeName) {
        if (instance==null||instance.isEmpty())  {
            System.out.println( "해당하는 채널이 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Channel find = instance.firstEntry().getValue();
            find.setChannelName(changeName);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            System.out.println("중복된 채널이 있습니다.");
            return false;
        }

    }
}
