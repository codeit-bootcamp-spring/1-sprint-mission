package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DataNotFoundException;
import com.sprint.mission.discodeit.exception.IdNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import groovyjarjarasm.asm.tree.TryCatchBlockNode;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService(){
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(Channel channel) {
        data.put(channel.getId(), channel);
        System.out.println(channel.getName() + " 채널이 오픈되었습니다.");
        return channel;
    }

    @Override
    public Channel readOne(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        data.values().forEach(channel -> {
            System.out.println(channel.getName());
        });

        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, Channel updatedChannel) {
        try{
            if(data.containsKey(id)){
                Channel existingChannel = data.get(id);
                existingChannel.setName(updatedChannel.getName());
                existingChannel.setDescription(updatedChannel.getDescription());
                updatedChannel.update();
                return existingChannel;
            }
        }catch (DataNotFoundException e){
            throw new DataNotFoundException("채널을 찾을 수 없습니다." + e);
        }
        return null;
    }

    @Override
    public void channelOwnerChange(UUID id, User owner){
        try {
            if(data.containsKey(id)){
                Channel existingChannel = data.get(id);
                User existingOwnerName = existingChannel.getOwner();
                existingChannel.setOwner(owner);
                existingChannel.update();
                System.out.println("채널 주인이 " + existingOwnerName.getUsername() + "님에서 " + owner.getUsername() + "님으로 변경되었습니다.");
            }
        }catch (DataNotFoundException e){
            throw new DataNotFoundException("채널을 찾을 수 없습니다." + e);
        }
    }

    @Override
    public boolean delete(UUID id) {
        try{
            String removename = data.get(id).getName();
            data.remove(id);
            System.out.println(removename +" 삭제가 완료되었습니다.");
            return true;
        } catch (NullPointerException e){
            System.out.println("유효하지 않은 ID 입니다..\n" + e);
        }
        return false;
    }

    @Override
    public void channelMemberJoin(UUID id, User joinUser){
        try {
            if (data.containsKey(id)) {
                Channel joinChannel = data.get(id);

                List<User> members = new ArrayList<>(joinChannel.getMember());
                if (!members.contains(joinUser)) {
                    members.add(joinUser);
                    System.out.println(joinChannel.getName() + " 채널에 " + joinUser.getUsername() + "님이 등록되었습니다.");
                } else {
                    System.out.println("User is already a member.");
                }
                joinChannel.setMember(members);
            }
        }catch (DataNotFoundException e){
            throw new DataNotFoundException("채널을 찾을 수 없습니다.");
        }
    }

    @Override
    public void channelMemberWithdrawal(UUID id, User withdrawalUser){

    }

}
