package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.User;

public class RepositoryManager {

    private final JCFUserRepository userRepository = new JCFUserRepository();
    private final JCFMessageRepository messageRepository = new JCFMessageRepository();
    private final JCFChannelRepository channelRepository = new JCFChannelRepository();

    public User createUser(String name, String password){
        User user = new User(name, password);
        return userRepository.saveUser(user);
    }

    public Channel createChannel(String name){
        // 채널 이름 중복 검증
        try {
            channelRepository.validateDuplicateName(name);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        Channel channel = new Channel(name);
        return channelRepository.register(channel);
    }


}
