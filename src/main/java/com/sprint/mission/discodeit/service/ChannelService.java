package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.User;

public interface ChannelService {
    public abstract void Create(User user, String channelName);

    // Read : 전체 채널 조회, 특정 채널 조회
    public abstract void ReadAll();
    public abstract void ReadChannel(String channelName);

    // Update : 특정 채널 이름 변경
    public abstract void Update(String channelName);

    // Delete : 전체 채널 삭제, 특정 채널 삭제
    public abstract void DeleteAll();
    public abstract void DeleteChannel(String channelName);
}
