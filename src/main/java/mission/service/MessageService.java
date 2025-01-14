package mission.service;


import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MessageService {
    //등록
    Message create(Message message);

    // 조회 단건
    Set<Message> findMessagesInChannel(Channel channel);

    //[ ] 조회(다건)
    Set<Message> findAll();

    //[ ] 수정
    Message update(UUID id, String newMessage);

    //[ ] 삭제
    void delete(Message message);
    //    void delete(UUID id, String message);
}
