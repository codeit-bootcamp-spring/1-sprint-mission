package mission.service.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.repository.jcf.JCFMessageRepository;
import mission.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {

    private final JCFMessageRepository messageRepository = new JCFMessageRepository();

    @Override
    public Message create(Message message) {
        return messageRepository.createMessage(message);
    }

    @Override
    public List<Message> findMessagesInChannel(Channel channel) {
        return messageRepository.findMessagesInChannel(channel);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Message findMessage(User writer, String writedMessage){
        for (Message message : writer.getMessages()) {
            if (message.getMessage().equals(writedMessage)){
                return message;
            }
        }
        return null;
    }

    public Message findMessageById(Channel channel, UUID messageId){
        Map<UUID, Message> messageMap = findMessagesInChannel(channel).stream()
                .collect(Collectors.toMap(
                        Message::getId,  // Message객체 id를 키
                        message -> message  // 파라미터 -> 반환값
                ));
        try {
            return messageMap.get(messageId);
        } catch (Exception e){
            throw new NullPointerException("MessageId를 잘못 입력했습니다");
        }
    }

    @Override
    public Message update(UUID messageId, String newMessage) {
        return messageRepository.updateMessage(messageId, newMessage);
    }

    @Override
    public void delete(Channel writedAt, UUID messageId, User writer) {
        Message deletingMessage = messageRepository.findMessageById(messageId);
        messageRepository.delete(writedAt, deletingMessage, writer);
    }
}
