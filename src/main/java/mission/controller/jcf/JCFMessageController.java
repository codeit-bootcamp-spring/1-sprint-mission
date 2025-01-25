package mission.controller.jcf;

import mission.entity.Message;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageController {

    private final JCFUserService userService = JCFUserService.getInstance();
    private final JCFChannelService channelService = JCFChannelService.getInstance();
    private final JCFMessageService messageService = JCFMessageService.getInstance();

    // 서비스간 의존 多
    public Message createMessage(UUID channelId, UUID userId, String message) {
        // 채널 찾고, user 찾고 (findById 이 과정에서 검증 다 끝남) => message 생성
        return messageService.createOrUpdate(Message.createMessage(channelService.findById(channelId), userService.findById(userId), message));
    }

    // Message 수정
    public Message updatedMessage(UUID messageId, String newString) {
        return messageService.update(messageId, newString);
    }

    // Message 찾는 것들
    public Message findMessageById(UUID messageId){
        return messageService.findById(messageId);
    }

    public Set<Message> findAll(){
        return messageService.findAll();
    }

    public Set<Message> findMessagesByUser(UUID userId) {
        return userService.findById(userId).getMessagesImmutable();
    }

    public List<Message> findMessageByUser_String(UUID userId, String writedMessage) {
        return findMessagesByUser(userId).stream()
                .filter(message -> message.getMessage().contains(writedMessage))
                .collect(Collectors.toList());
    }

    public Set<Message> findMessagesInChannel(UUID channelId) {
        return messageService.findMessagesInChannel(channelService.findById(channelId));
    }

    public Set<Message> findMessagesContainingM_InChannel(UUID channelId, String findString) {
        return messageService.findMessagesByContentInChannel(channelService.findById(channelId), findString);
    }

    /**
     * 삭제
     */

    public void delete(UUID messageId){
        messageService.delete(messageId);
    }

}
