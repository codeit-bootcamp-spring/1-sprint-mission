package mission.controller.jcf;

import mission.entity.Channel;
import mission.entity.User;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;

import java.util.Set;
import java.util.UUID;

public class JCFUserController {

    private final JCFUserService userService = new JCFUserService();
    private final JCFMessageService messageService = new JCFMessageService();
    private final JCFChannelService channelService = new JCFChannelService();

    public User createUser(String name, String password) {
        // 닉넴 중복 검증 필요할 경우 => userService.validateDuplicateName(name);
        // 검증에서 예외 터지면 유저 생성 X
        User user = new User(name, password);
        return userService.createOrUpdate(user);
    }

    // User 개인정보 변경
    public User updateUserNamePW(UUID id, String newName, String password) {
        User updatingUser = userService.findById(id);
        if (updatingUser == null) {
            return null;
        }
        updatingUser.setNamePassword(newName, password);
        return userService.update(updatingUser);
    }

    public User findUserById(UUID id) {
        return userService.findById(id);
    }

    public Set<User> findUserByName(String findName){
        return userService.findUsersByName(findName);
    }

    public User findUserByNamePW(String name, String password) {
        // Set<User> findUsersByName = findUserByName(name); => 필터링 가능
        // 이렇게 컨트롤러에서 바로 찾을 수 있지만 userService...라는 역할..에
        return userService.findByNamePW(name, password);
    }

    public Set<User> findAllUsers() {
        return userService.findAll();
    }

    public Set<User> findUsersInChannel(UUID channelId) {
        return findChannelById(channelId).getUsersImmutable();
    }


    public void deleteUser(UUID id, String nickName, String password) {
        User deletingUser = findUserById(id);
        if (!(deletingUser.getName().equals(nickName) && deletingUser.getPassword().equals(password))) {
            System.out.println("닉네임 or 비밀번호가 틀렸습니다.");
        } else {
            userService.delete(deletingUser);
        }
    }

    // 강퇴 및 채널 탈퇴
    public void drops(UUID channel_Id, UUID droppingUser_Id) {
        Channel droppingChannel = findChannelById(channel_Id);
        User droppingUser = findUserById(droppingUser_Id);
        droppingUser.removeChannel(droppingChannel);
    }


    // 모든 채널 탈퇴
    public void dropsAllByUser(UUID droppingUser_Id){
        User user = findUserById(droppingUser_Id);
        user.removeAllChannel();
    }

    // 유저 등록
    public void addChannelByUser(UUID channelId, UUID userId) {
        Channel channel = findChannelById(channelId);
        findUserById(userId).addChannel(channel); // 양방향으로 더하는 로직
    }

}
