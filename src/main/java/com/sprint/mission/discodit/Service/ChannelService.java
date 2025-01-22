package com.sprint.mission.discodit.Service;

import com.sprint.mission.discodit.Entity.Channel;
import com.sprint.mission.discodit.Entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    //도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요
    public UUID createChannel(String userName);
    public Channel getChannel(UUID id);
    public List<Channel> getChannels();
    public void setChannel(UUID id, String name);
    public void deleteChannel(UUID id);
}
