package mission.service;


import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MessageService {
    //등록
    Message createOrUpdate(Message message) throws IOException;

    // 조회 단건
    Message findById(UUID messageId);
    //[ ] 조회(다건)
    Set<Message> findMessagesInChannel(Channel channel);
    Set<Message> findAll();

    //[ ] 수정
    Message update(UUID messageId, String newMassage);
 //   Message update(Message message);

    //[ ] 삭제
 //   void delete(Message message);

    void delete(UUID messageId);
    //    void delete(UUID id, String message);
}
