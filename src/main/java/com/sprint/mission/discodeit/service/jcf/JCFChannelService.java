package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    //final을 사용하면 해당  entity는 수정할 수 없다.
    final List<Channel> channeldata;

    public JCFChannelService() { //data필드 생성자초기화
        this.channeldata= new ArrayList<>();
    }


    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        channeldata.add(channel);
        System.out.println(" 채널명: " + channel.getChannelName() + " 채널이 추가되었습니다.");
        return channel;
    }

    @Override
    public Channel readChannel(UUID id) { //

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
    public Channel modifyChannel(UUID id, String name) { //null 방어
        Channel target = readChannel(id);
        String oriName=target.getChannelName();
        target.updateName(name);
        target.updateUpdatedAt(); //시간 업데이트
        System.out.println("채널이름 변경: " + oriName+ " -> " + name );
        return target;
    }

    @Override
    public void deleteChannel(UUID id) {
        String name= readChannel(id).getChannelName();
        boolean isDeleted = this.channeldata.removeIf(ch -> ch.getChId().equals(id));
        
        if(isDeleted) {
            System.out.println(name + " 채널이 삭제되었습니다.");
        }else{
            System.out.println(name + " 채널 삭제 실패하였습니다.");
        }
    }


}
