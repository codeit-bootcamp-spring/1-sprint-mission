package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;

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
        Channel writeAt = channelService.findById(channelId);
        return messageService.create(writeAt, writer, message);
    }




    public User findUserById(UUID id){
        return userService.findById(id);
    }

    public User findUserByNamePW(String name, String password){
        return userService.findByNamePW(name, password);
    }

    public List<User> findAllUsers(){
        return userService.findAll();
    }

    public Channel findChannelById(UUID id){
        return channelService.findById(id);
    }

    public Channel findChannelByName(String channelName){
        return channelService.findByName(channelName);
    }

    public List<Channel> findAllChannel(){
        return channelService.findAll();
    }

}
