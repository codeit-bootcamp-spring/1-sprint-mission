package mission.controller.jcf;

import mission.controller.MessageController;
import mission.entity.Message;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageController implements MessageController {

    private final JCFUserService userService = JCFUserService.getInstance();
    private final JCFChannelService channelService = JCFChannelService.getInstance();
    private final JCFMessageService messageService = JCFMessageService.getInstance();

    @Override
    public void create(UUID channelId, UUID userId, String message) {
        // 채널 찾고, user 찾고 (findById 이 과정에서 검증 다 끝남) => message 생성
        messageService.createOrUpdate(Message.createMessage(channelService.findById(channelId), userService.findById(userId), message));
    }

    @Override
    public void update(UUID messageId, String newString) {
        messageService.update(messageId, newString);
    }

    @Override
    public Message findById(UUID messageId){
        return messageService.findById(messageId);
    }

    @Override
    public Set<Message> findAll(){
        return messageService.findAll();
    }

    @Override
    public Set<Message> findMessagesByUser(UUID userId) {
        return userService.findById(userId).getMessagesImmutable();
    }

    @Override
    public Set<Message> findMessagesInChannel(UUID channelId) {
        return messageService.findMessagesInChannel(channelService.findById(channelId));
    }

    @Override
    public Set<Message> findContainingMessageInChannel(UUID channelId, String writedMessage) {
        return findMessagesInChannel(channelId).stream()
                .filter(message -> message.getMessage().contains(writedMessage))
                .collect(Collectors.toCollection(HashSet::new));
    }


    @Override
    public void delete(UUID messageId){
        messageService.delete(messageId);
    }

}
