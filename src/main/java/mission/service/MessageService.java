package mission.service;


import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    //등록
    Message create(Channel channel, User user, String message);

    // 조회 단건
    Message findMessage(String message);

    //[ ] 조회(다건)
    List<Message> findAll();

    //[ ] 수정
    Message update(UUID id, String newMessage);

    //[ ] 삭제
    void delete(UUID id, String message);

}
