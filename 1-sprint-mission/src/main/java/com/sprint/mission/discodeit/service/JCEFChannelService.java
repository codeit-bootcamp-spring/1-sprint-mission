package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCEFChannelService implements ChannelService{

    private final List<Channel> ChannelList=new ArrayList<>();

    @Override
    public void CreateChannelDefault(String name) {
        if(find_Channel(name).isEmpty()){
            Channel new_channel=Channel.CreateDefaultChannel(name);
            ChannelList.add(new_channel);
        }else {
            System.out.println("이미 존재하는 채널입니다.");
        }

    }


    @Override
    public <T>String ReadChannel(T key) {
        StringBuilder list= new StringBuilder();
        for(Channel now:ChannelList){
            if((key instanceof String&&now.getChannelName().equals(key))||
            (key instanceof UUID&&now.getId().equals(key))){
                list.append(now.toString()).append("\n");
            }
        }
        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 채널이 없습니다.";
    }

    @Override
    public String ReadChannelAll() {
        StringBuilder list= new StringBuilder();
        for(Channel now:ChannelList){
            list.append(now.toString()).append("\n");

        }
        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "등록된 채널이 없습니다.";
    }

    @Override
    public void UpdateChannelName(UUID id, String name) {
        ArrayList<Channel> instance = find_Channel(id);
        updateChannel(instance,name);
    }
    public void UpdateChannelName(String ChannelName, String name) {
        ArrayList<Channel> instance = find_Channel(ChannelName);
        updateChannel(instance,name);
    }
    @Override
    public void DeleteChannel(UUID id) {
        ArrayList<Channel> instance = find_Channel(id);
        deleteChannel(instance);
    }

    @Override
    public void DeleteChannel(String Name) {
        ArrayList<Channel> instance = find_Channel(Name);
        deleteChannel(instance);
    }


    private ArrayList<Channel> find_Channel(String name) {
        return new ArrayList<Channel>(
                ChannelList.stream().filter(channel -> channel.getChannelName().equals(name)).toList()
        );
    }


    private ArrayList<Channel> find_Channel(UUID ID) {
        return new ArrayList<Channel>(
                ChannelList.stream().filter(channel -> channel.getId().equals(ID)).toList()
        );
    }


    private void deleteChannel(ArrayList<Channel> instance) {
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 채널가 없습니다.");
        }else if(instance.size()==1){
            ChannelList.remove(instance.get(0));
            System.out.println("성공적으로 삭제했습니다.");
        }
    }

    private void updateChannel(ArrayList<Channel> instance, String changeName) {
        if (instance==null||instance.isEmpty())  {
            System.out.println( "해당하는 채널이 없습니다.");
        } else if (instance.size() == 1) {
            Channel find = instance.get(0);
            find.setChannelName(changeName);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
        }
    }
}
