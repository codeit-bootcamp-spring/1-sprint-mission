package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.MessageEntity;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    // 메시지 추가
    void addMessage(MessageEntity message);

    // 메시지 갱신
    void updateMessage(UUID id, UUID userId, UUID channelId, String content);

    // 메시지 내용 갱신
    void updateMessageContent(UUID id, String content);

    // 메시지 채널 갱신
    void updateMessageChannel(UUID id, UUID channelId);

    // 메시지 채널 및 내용 갱신
    void updateMessageChannel(UUID id, UUID channelId, String content);

    // 메시지 유저 갱신
    void updateMessageUser(UUID id, UUID userId);

    // 메시지 삭제
    void deleteMessage(UUID id);

    // 메시지 삭제(Flg Update 방식)
    void deleteMessageWithFlg(UUID id);

    // 메시지 호출
    MessageEntity getMessageById(UUID id);

    // 메시지 호출 (Channel 에 소속된 메시지를 모두 호출)
    List<MessageEntity> getMessageByChannelId(UUID channelId);

    // 메시지 호출 (Flg 방식)
    MessageEntity getMessageByIdWithFlg(UUID id);

    // 메시지 호출 (Flg 방식으로 Channel 에 소속된 메시지를 모두 호출)
    List<MessageEntity> getMessageByChannelIdWithFlg(UUID channelId);

    // 메시지 삭제 (Channel 이 삭제될 때, 해당 Channel 에 소속된 메시지를 모두 삭제)
    void deleteMessageByChannelId(UUID channelId);

    // 메시지 삭제(Flg Update 방식) (Channel 이 삭제될 때, 해당 Channel 에 소속된 메시지를 모두 삭제)
    void deleteMessageByChannelIdWithFlg(UUID channelId);

}
