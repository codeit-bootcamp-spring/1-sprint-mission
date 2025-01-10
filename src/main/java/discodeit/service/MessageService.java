package discodeit.service;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;

public interface MessageService {
    // Create
    Message createMessage(User user, Channel channel, String content); // 메세지 전송
    // Read
    void viewMessage(Message message);  // 메세지 단건 조회
    void viewAllMessages(); // 모든 메세지 조회
    // Update
    void updateMessage(User user, Message message, String newContent);    // 메시지 수정
    // Delete
    void deleteMessage(Message message);    // 메세지 삭제
}
