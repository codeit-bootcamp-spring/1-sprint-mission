package mission.service.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;

import java.util.List;
import java.util.UUID;

public class ProjectManager {

    private final JCFUserService userService = new JCFUserService();
    private final JCFMessageService messageService = new JCFMessageService();
    private final JCFChannelService channelService = new JCFChannelService();

    public User createUser(String name, String password) {
        // 여기서 예외 터지면 유저 생성 X
        userService.validateDuplicateName(name);
        User user = new User(name, password);
        return userService.create(user);
    }

    public Channel createChannel(String name) {
        channelService.validateDuplicateName(name);
        return channelService.create(new Channel(name));
    }

    public Message createMessage(UUID channelId, UUID userId, String message) {
        // 채널 찾고, user 찾고 (find~ 메서드 이 과정에서 검증 다 끝남) => message 생성
        Message writeMessage = Message.createMessage(findChannelById(channelId), findUserById(userId), message);
        return messageService.create(writeMessage);
    }

    /**
     * 채널과 유저 서로 추가
     */
    public void addChannelByUser(UUID channelId, UUID userId) {
        Channel channel = findChannelById(channelId);
        findUserById(userId).addChannel(channel);
        // 이 기능도 포함 : channel.getUserList().add(user);
        // 따라서 addUserByChannel 필요 없음
    }

    /**
     * updating
     */

    // User 개인정보 변경
    public User updateUserNamePW(UUID id, String newName, String password) {
        User updatingUser = findUserById(id);
        // oldName 설정 이유 : 레포지토리에 userName 리스트를 삭제해야하기 때문
        updatingUser.setOldName(updatingUser.getName());

        updatingUser.setNamePassword(newName, password);
        return userService.update(updatingUser);
    }

    // Channel 이름 변경
    public Channel updateChannelName(UUID channelId, String newName) {
        // 채널 찾기
        Channel updatedChannel = findChannelById(channelId);

        // 옛 이름 설정, 새 이름 설정  ==> 중복검사는 repository에서 할 것
        // @트랜잭션 도입 예정이면 중복이면 어차피 롤백 나오니까 먼저 nameSet하고 검증해도 상관없을거라는 생각
        updatedChannel.setOldName(updatedChannel.getOldName());
        updatedChannel.setName(newName);
        return channelService.update(updatedChannel);
    }

    // Message 수정
    public Message updatedMessage(UUID channelId, UUID messageId, String newString) {
        Message updatingMessage = findMessageByC_M_Id(channelId, messageId);
        return messageService.update(messageId, newString);
    }

    /**
     * finding
     */
    // User 찾는 것들
    public User findUserById(UUID id) {
        return userService.findById(id);
    }

    public User findUserByNamePW(String name, String password) {
        return userService.findByNamePW(name, password);
    }

    public List<User> findAllUsers() {
        return userService.findAll();
    }

    public List<User> findUsersInChannel(UUID channelId) {
        return findChannelById(channelId).getUserList();
    }

    // Channel 찾는 것들
    public Channel findChannelById(UUID id) {
        return channelService.findById(id);
    }

    public Channel findChannelByName(String channelName) {
        return channelService.findByName(channelName);
    }

    public List<Channel> findAllChannel() {
        return channelService.findAll();
    }

    // Message 찾는 것들
    public List<Message> findUserMessage(UUID userId) {
        return findUserById(userId).getMessages();
    }

    public Message findMessageByC_M_Id(UUID channelId, UUID messageId) {
        return messageService.findMessageById(findChannelById(channelId), messageId);
    }

    public Message findMessageByUser_String(UUID userId, String writedMessage) {
        return messageService.findMessage(findUserById(userId), writedMessage);
    }

    public List<Message> findMessagesInChannel(UUID channelId) {
        return messageService.findMessagesInChannel(findChannelById(channelId));
    }

    /**
     * delete
     */
    public void deleteUser(UUID id, String nickName, String password) {
        User deletingUser = findUserById(id);
        if (!(deletingUser.getName().equals(nickName) && deletingUser.getPassword().equals(password))){
            System.out.println("닉네임 or 비밀번호가 틀렸습니다.");
        } else {
            userService.delete(deletingUser);
        }
    }

    public void deleteChannel(UUID channelId) {
        Channel deletingChannel = findChannelById(channelId);
        // 각 user가 갖고 있는 channelList에서 삭제될 채절 remove
        for (User user : deletingChannel.getUserList()) {
            user.getChannels().remove(deletingChannel);
        }

        // 그 채널에서 생겼더 메시지도 삭제해야될지, 그래도 기록으로 보관하니 삭제하지 말지
        //List<Message> messagesInChannel = findMessageInChannel(channelId);
        channelService.deleteById(channelId);
    }

    public void deleteMessage(UUID messageId, UUID channelId) {
        Message deletingMessage = findMessageByC_M_Id(channelId, messageId);
        // userId 검증 추가로 할 필요도 없겠다. createMessage에서 이미 필요
        messageService.delete(deletingMessage);
    }

}
