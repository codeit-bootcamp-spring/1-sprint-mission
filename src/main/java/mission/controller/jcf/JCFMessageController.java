package mission.controller.jcf;

import mission.entity.Message;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;

import java.util.Set;
import java.util.UUID;

public class JCFMessageController {

    private final JCFUserService userService = new JCFUserService();
    private final JCFMessageService messageService = new JCFMessageService();
    private final JCFChannelService channelService = new JCFChannelService();

    // 서비스간 의존 多
    public Message createMessage(UUID channelId, UUID userId, String message) {
        // 채널 찾고, user 찾고 (find~ 메서드 이 과정에서 검증 다 끝남) => message 생성
        return messageService.create(Message.createMessage(channelService.findById(channelId), userService.findById(userId), message));
    }


    // Message 수정
    public Message updatedMessage(UUID channelId, UUID messageId, String newString) {
        Message updatingMessage = findMessageByC_M_Id(channelId, messageId);
        return messageService.update(messageId, newString);
    }

    public Message findMessageByC_M_Id(UUID channelId, UUID messageId) {
        return messageService.findMessageById(channelService.findById(channelId), messageId);
    }

    // Message 찾는 것들
    public Set<Message> findUserMessage(UUID userId) {
        return findUserById(userId).getMessages();
    }


    public Message findMessageByUser_String(UUID userId, String writedMessage) {
        return messageService.findMessage(findUserById(userId), writedMessage);
    }

    public Set<Message> findMessagesInChannel(UUID channelId) {
        return messageService.findMessagesInChannel(findChannelById(channelId));
    }

}
