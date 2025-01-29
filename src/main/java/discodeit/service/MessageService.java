package discodeit.service;

import discodeit.enity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // Create
    UUID createMessage(UUID userId, UUID channelId, String content); // 메세지 전송

    // Read
    String getMessageById(UUID messageId);  // 메세지 단건 조회

    List<String> getAllMessages(); // 모든 메세지 조회

    // Update
    void updateMessage(UUID userId, UUID messageId, String newContent);    // 메시지 수정

    // Delete
    void deleteMessage(UUID messageId);    // 메세지 삭제

    Message findById(UUID messageId);

    List<String> getMessagesByChannelId(UUID channelId);    // 특정 채널에 속한 메세지들 형식에 맞춰 반환

    void deleteMessagesInChannel(UUID channelId);   // 채널 삭제 시 해당 채널에 속한 메세지를 모두 삭제
}
