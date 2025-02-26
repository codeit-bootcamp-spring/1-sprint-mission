package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    protected Map<UUID,Channel> channelList=new LinkedHashMap<>();
    protected static final Set<UUID> existChannelIdCheck = new HashSet<>();

    @Override
    public void createNewChannel(String name) {
        Channel newChannel;
        do{
            newChannel=Channel.createDefaultChannel(name);
        }while(existChannelIdCheck.contains(newChannel.getId()));

        channelList.put(newChannel.getId(),newChannel);
        existChannelIdCheck.add(newChannel.getId());

    }


    @Override
    public <T>String readChannel(T key) {
        StringBuilder list= new StringBuilder();
        LinkedHashMap<UUID,Channel> foundChannel=new LinkedHashMap<>();
        if (key instanceof String) {
            foundChannel = findChannel((String) key);
        } else if (key instanceof UUID) {
            foundChannel = findChannel((UUID) key);
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
    public String readChannelAll() {
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
    public boolean updateChannelName(UUID id, String name) {
        LinkedHashMap<UUID,Channel> instance = findChannel(id);
        return updateChannelInfo(instance,name);
    }
    public boolean updateChannelName(String ChannelName, String name) {
        LinkedHashMap<UUID,Channel> instance = findChannel(ChannelName);
        return updateChannelInfo(instance,name);
    }
    @Override
    public boolean deleteChannel(UUID id) {
        LinkedHashMap<UUID,Channel> instance = findChannel(id);
        return deleteChannelInfo(instance);
    }

    @Override
    public boolean deleteChannel(String Name) {
        LinkedHashMap<UUID,Channel> instance = findChannel(Name);
        return deleteChannelInfo(instance);
    }


    protected LinkedHashMap<UUID,Channel> findChannel(String name) {
        LinkedHashMap<UUID,Channel> findChannel=new LinkedHashMap<>();
        for(Channel channel:channelList.values()){
            if(channel.getChannelName().equals(name)){
                findChannel.put(channel.getId(),channel);
            }
        }
        return findChannel;
    }


    protected LinkedHashMap<UUID,Channel> findChannel(UUID ID) {
        LinkedHashMap<UUID,Channel> findChannel=new LinkedHashMap<>();
        findChannel.put(ID,channelList.get(ID));
        return findChannel;
    }


    protected boolean deleteChannelInfo(LinkedHashMap<UUID,Channel> instance) {
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 채널이 없습니다.");
            return false;
        }else if(instance.size()==1){
            Channel firstChannel=instance.entrySet().iterator().next().getValue();
            existChannelIdCheck.remove(firstChannel.getId());
            channelList.remove(firstChannel.getId());
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        }else{
            System.out.println("중복된 채널이 있습니다.");
            return false;
        }
    }

    protected boolean updateChannelInfo(LinkedHashMap<UUID,Channel> instance, String changeName) {
        if (instance==null||instance.isEmpty())  {
            System.out.println( "해당하는 채널이 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Channel firstChannel = instance.entrySet().iterator().next().getValue();
            firstChannel.updateChannelName(changeName);
            firstChannel.updateUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            System.out.println("중복된 채널이 있습니다.");
            return false;
        }

    }
}