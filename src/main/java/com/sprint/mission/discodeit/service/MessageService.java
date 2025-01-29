package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageResDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageService {

    // 메시지 유효성 확인 -> 메시지 content 내용이 비었는지만 확인

    // 메시지 생성
    public Long createMessage(User author, Channel channel, String content);

    // 조회
    // 단일 조회
    public MessageResDTO getMessage(Long id);

    public MessageResDTO getMessage(String uuid);

    public Message findMessageById(Long id);

    // 전체 조회
    public List<MessageResDTO> getAllMessage(); // 그냥 모든 메시지

    public List<MessageResDTO> getChannelMessage(String channelName);   // 채널의 모든 메시지

    Optional<Map.Entry<Long, Message>> findMessageByUUID(String uuid);

    // 메시지 수정
    public boolean updateMessage(Long id, MessageUpdateDTO updateInfo);

    // 메시지 삭제
    public MessageResDTO deleteMessage(Long id);

    public MessageResDTO deleteMessage(String uuid);
}
