package com.spirnt.mission.discodeit.jcf;

import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService(){
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(Channel channel) {
        data.put(channel.getId(), channel);
        System.out.println(channel.getChannelName() + " 채널이 오픈되었습니다.");
        return channel;
    }

    @Override
    public Channel read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, Channel updatedChannel) {
        if(data.containsKey(id)){
            Channel existingChannel = data.get(id);
            existingChannel.setChannelName(updatedChannel.getChannelName());
            existingChannel.setDescription(updatedChannel.getDescription());
            updatedChannel.update();
            return existingChannel;
        }
        return null;
    }

    @Override
    public void channelOwnerChange(UUID id, User owner){
        if(data.containsKey(id)){
            Channel existingChannel = data.get(id);
            User existingOwnerName = existingChannel.getOwner();
            existingChannel.setOwner(owner);
            existingChannel.update();
            System.out.println("채널 주인이 " + existingOwnerName.getUsername() + "님에서 " + owner.getUsername() + "님으로 변경되었습니다.");
        }
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    @Override
    public void channelMemberJoin(UUID id, User joinUser){
        if(data.containsKey(id)){
            Channel joinChannel = data.get(id);

            List<User> members = new ArrayList<>(joinChannel.getMember());
            if (!members.contains(joinUser)) {
                members.add(joinUser);
                System.out.println(joinChannel.getChannelName() + " 채널에 " + joinUser.getUsername() + "님이 등록되었습니다.");
            } else {
                System.out.println("User is already a member.");
            }
            joinChannel.setMember(members);
        }
    }

    @Override
    public void channelMemberWithdrawal(UUID id, User withdrawalUser){

    }

}
