package discodeit.service;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;

import java.util.UUID;

public interface MessageService {
    // Create
    UUID createMessage(UUID userId, UUID channelId, String content); // 메세지 전송

    // Read
    void viewMessage(UUID messageId);  // 메세지 단건 조회

    void viewAllMessages(); // 모든 메세지 조회

    // Update
    void updateMessage(UUID userId, UUID messageId, String newContent);    // 메시지 수정

    // Delete
    void deleteMessage(UUID messageId);    // 메세지 삭제

    boolean validateMessage(UUID messageId);   // 실제 존재하는 메세지인지 검증
}
