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
        User writer = findUserById(userId);
        Channel writeAt = findChannelById(channelId);
        return messageService.create(writeAt, writer, message);
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
        User findUser = findUserById(userId);
        return findUser.getMessages();
    }

    public Message findMessage(UUID userId, String writedMessage){
        User findUser = findUserById(userId);
        return messageService.findMessage(findUser, writedMessage);
    }

    public List<Message> findMessageInChannel(UUID channelId){
        Channel findChannel = findChannelById(channelId);
        return messageService.findMessagesInChannel(findChannel);
    }

    /**
     * delete
     */
    public void deleteUser(UUID id, String nickName, String password){
        userService.delete(id, nickName, password);
    }

}
