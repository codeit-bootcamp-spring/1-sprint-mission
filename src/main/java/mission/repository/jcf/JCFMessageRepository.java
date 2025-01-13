package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.repository.MessageRepository;

import java.util.HashMap;
import java.util.Map;

public class JCFMessageRepository implements MessageRepository {

    private final Map<Channel, Message> messageMap = new HashMap<>();

    public Message createMessage(Channel writeAt, User wirter, String writedMessage){
        Message createdMessage = new Message(writedMessage);
        wirter.createMessage(createdMessage, writeAt);
        messageMap.put(createdMessage.getWritedAt(), createdMessage);
        return createdMessage;
    }
}
