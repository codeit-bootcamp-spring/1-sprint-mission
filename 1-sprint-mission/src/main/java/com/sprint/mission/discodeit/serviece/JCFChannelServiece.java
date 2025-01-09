package com.sprint.mission.discodeit.serviece;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.UUID;

public class JCFChannelServiece implements ChannelService{

    final ArrayList<Channel> ChannelList=new ArrayList<>();

    @Override
    public void CreateChaneel(String name) {
        Channel new_channel=new Channel(name);
        ChannelList.add(new_channel);
    }

    @Override
    public String ReadChannel(UUID id) {
        StringBuilder list= new StringBuilder();
        boolean flag=false;
        for(Channel now:ChannelList){
            flag=true;
            if(now.getId().equals(id)){
                list.append(now.DisplayChannelInfo()).append("\n");
            }
        }
        if(flag&& !list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 채널이 없습니다.";
    }

    @Override
    public String ReadChannel(String name) {
        StringBuilder list= new StringBuilder();
        boolean flag=false;
        for(Channel now:ChannelList){
            flag=true;
            if(now.getChannelName().equals(name)){
                list.append(now.DisplayChannelInfo()).append("\n");
            }
        }
        if(flag&& !list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 채널이 없습니다.";
    }

    @Override
    public String ReadChannelAll() {
        StringBuilder list= new StringBuilder();
        boolean flag=false;
        for(Channel now:ChannelList){
            flag=true;

            list.append(now.DisplayChannelInfo()).append("\n");

        }
        if(flag&& !list.toString().isEmpty())
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
        if(instance.isEmpty()){
            System.out.println("해당하는 채널가 없습니다.");
        }else if(instance.size()==1){
            ChannelList.remove(instance.get(0));
            System.out.println("성공적으로 삭제했습니다.");
        }else{
            System.out.println("중복된 채널이 있습니다.");
        }
    }

    private void updateChannel(ArrayList<Channel> instance, String changeName) {
        if (instance.isEmpty()) {
            System.out.println( "해당하는 채널이 없습니다.");
        } else if (instance.size() == 1) {
            Channel find = instance.get(0);
            find.setChannelName(changeName);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
        } else {
            System.out.println("중복된 채널이 있습니다.");
        }
    }
}
