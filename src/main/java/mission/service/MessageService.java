package mission.service;


import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    //등록
    Message create(Message message);

    // 조회 단건
    List<Message> findMessagesInChannel(Channel channel);

    //[ ] 조회(다건)
    List<Message> findAll();

    //[ ] 수정
    Message update(UUID id, String newMessage);

    //[ ] 삭제
    void delete(Channel writedAt, UUID messageId, User writer);
    //    void delete(UUID id, String message);
}
