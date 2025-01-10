package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {

    //final을 사용하면 해당  entity는 수정할 수 없다.
    final List<Channel> channeldata;

    public JCFChannelService() { //data필드 생성자초기화
        this.channeldata= new ArrayList<>();
    }


    @Override
    public void createChannel(Channel channel) {
        channeldata.add(channel);
        System.out.println(" 채널명: " + channel.getChName() + " 채널이 추가되었습니다.");
    }

    @Override
    public Channel readChannel(String id) { //

        return
                this.channeldata.stream()
                        .filter(ch -> ch.getChId().equals(id))
                        .findFirst()
                        .orElse(null);


    }

    @Override
    public List<Channel> readAllChannel() {
        return channeldata;
    }

    //이름 및 시간 업데이트?
    @Override
    public void modifyChannel(String id, String name) {
        Channel target = readChannel(id);
        target.updateName(name);
        target.updateUpdatedAt(); //시간 업데이트

    }

    @Override
    public void deleteChannel(String id) {
        String name= readChannel(id).getChName();
        boolean isDeleted = this.channeldata.removeIf(ch -> ch.getChId().equals(id));
        
        if(isDeleted) {
            System.out.println(name + " 채널이 삭제되었습니다.");
        }else{
            System.out.println(name + " 채널 삭제 실패하였습니다.");
        }
    }

    @Override
    public void addUser(Channel channel, User user) {
        channel.getUserList().add(user); //해당 채널에 유저 추가
    }

}
