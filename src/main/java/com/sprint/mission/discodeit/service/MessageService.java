package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

public interface MessageService {
    public abstract void Create(Channel channel, String messageText);

    // Read : 전체 메세지 조회, 특정 메세지 읽기
    public abstract int ReadAll();

    public abstract Message ReadMessage(int num);

    // Update : 특정 메세지 수정
    public abstract void Update(int num);

    // Delete : 전체 메세지 삭제, 특정 메세지 삭제
    public abstract void DeleteAll();
    public abstract void DeleteMessage(int num);
}
