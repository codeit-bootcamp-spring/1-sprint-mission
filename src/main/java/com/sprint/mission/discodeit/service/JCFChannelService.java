package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService{
    private final List<Channel> data;

    public JCFChannelService(){
        this.data = new ArrayList<>();
    }

    public void createChannel(Channel channel){

    }
    //채널 삭제
    public void deleteChannel(Channel channel){

    }
    //채널 수정
    public void updateChannel(Channel channel){

    }
    //채널 목록 확인
    public List<Channel> getAllChannel(){

    }
}
