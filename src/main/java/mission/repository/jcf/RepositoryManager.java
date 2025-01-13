package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.UUID;

public class RepositoryManager {

    private final JCFUserRepository userRepository = new JCFUserRepository();
    private final JCFUserService userService = new JCFUserService();
    private final JCFMessageRepository messageRepository = new JCFMessageRepository();
    private final JCFMessageService messageService = new JCFMessageService();
    private final JCFChannelRepository channelRepository = new JCFChannelRepository();
    private final JCFChannelService channelService = new JCFChannelService();

    public User createUser(String name, String password){
        return userService.create(name, password);
    }

    public Channel createChannel(String name){
        return channelService.create(name);
    }

    public Message createMessage(UUID channelId, UUID userId, String message){
        // 채널 찾고, user 찾고
        return messageService.create(findChannelById(channelId), findUserById(userId), message);
    }

    /**
     * updating
     */

    // User 개인정보 변경
    public User updateUserNamePW(UUID id, String newName, String password){
        return userService.update(id, newName, password);
    }

    // Channel 이름 변경
    public Channel updateChannelName(UUID channelId, String newName){
        return channelService.update(channelId, newName);
    }

    // Message 수정
    public Message updatedMessage(UUID messageId, String newString){
        return messageService.update(messageId, newString);
    }

    /**
     * finding
     */
    // User 찾는 것들
    public User findUserById(UUID id){
        return userService.findById(id);
    }

    public User findUserByNamePW(String name, String password){
        return userService.findByNamePW(name, password);
    }

    public List<User> findAllUsers(){
        return userService.findAll();
    }

    public List<User> findUsersInChannel(UUID channelId){
        return findChannelById(channelId).getUserList();
    }

    // Channel 찾는 것들
    public Channel findChannelById(UUID id){
        return channelService.findById(id);
    }

    public Channel findChannelByName(String channelName){
        return channelService.findByName(channelName);
    }

    public List<Channel> findAllChannel(){
        return channelService.findAll();
    }

    // Message 찾는 것들
    public List<Message> findUserMessage(UUID userId){
        return findUserById(userId).getMessages();
    }

    public Message findMessage(UUID userId, String writedMessage){
        return messageService.findMessage(findUserById(userId), writedMessage);
    }

    public List<Message> findMessageInChannel(UUID channelId){
        return messageService.findMessagesInChannel(findChannelById(channelId));
    }

    /**
     * delete
     */
    public void deleteUser(UUID id, String nickName, String password){
        userService.delete(id, nickName, password);
    }

    public void deleteChannel(UUID channelId){
        Channel deletingChannel = findChannelById(channelId);
        // user들의 channel에서 삭제
        for (User user : deletingChannel.getUserList()) {
            user.getChannels().remove(deletingChannel);
        }
        // 그 채널에 있는  메시지도 삭제해야될지, 그래도 기록으로 보관하니 삭제하지 말지
        //List<Message> messagesInChannel = messageService.findMessagesInChannel(deletingChannel);
        channelService.deleteById(channelId);
    }

    public void deleteMessage(UUID messageId, UUID channelId, UUID userId){
        messageService.delete(findChannelById(channelId), messageId, findUserById(userId));
        //writer.getMessages().remove(me)
    }

    /**
     * 채널과 유저 서로 추가
     */
    public void addChannelByUser(UUID channelId, UUID userId){
        Channel channel = findChannelById(channelId);

        findUserById(userId).addChannel(channel);
        // 이 기능도 포함 : channel.getUserList().add(user);
        // 따라서 addUserByChannel 필요 없음
    }

}
