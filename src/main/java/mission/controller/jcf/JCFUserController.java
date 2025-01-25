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
        User user = new User(name, password);
        return userService.createOrUpdate(user);
    }

    // User 개인정보 변경
    public User updateUserNamePW(UUID id, String newName, String password) {
        User updatingUser = userService.findById(id);
        updatingUser.setNamePassword(newName, password);
        return userService.update(updatingUser);
    }

    /**
     * 조회
     */
    public User findUserById(UUID id) {
        return userService.findById(id);
    }

    public Set<User> findUsersByName(String findName){
        return userService.findUsersByName(findName);
    }

    public User findUserByNamePW(String name, String password) {
        return findUsersByName(name).stream()
                .filter(user -> user.getPassword().equals(password))
                .findAny().orElse(null);
    }

    public Set<User> findAllUsers() {
        return userService.findAll();
    }

    public Set<User> findUsersInChannel(UUID channelId) {
        return channelService.findById(channelId).getUsersImmutable();
    }


    /**
     * 삭제
     */
    public void deleteUser(UUID id, String nickName, String password) {
        User deletingUser = findUserById(id); // 이 메서드에서 id 검증
        if (!(deletingUser.getName().equals(nickName) && deletingUser.getPassword().equals(password))) {
            System.out.println("닉네임 or 비밀번호가 틀렸습니다.");
            // 이 메서드에서 닉넴, 비번 검증
        } else {
            userService.delete(deletingUser);
        }
    }

    /**
     *  채널과 연계
     */

    // 강퇴 및 채널 탈퇴
    public void drops(UUID channel_Id, UUID droppingUser_Id) {
        Channel droppingChannel = channelService.findById(channel_Id);
        User droppingUser = findUserById(droppingUser_Id);
        droppingUser.removeChannel(droppingChannel);
        // removeChannel 양방향 설정
    }

    // 모든 채널 탈퇴
    public void dropsAllByUser(UUID droppingUser_Id){
        User user = findUserById(droppingUser_Id);
        user.removeAllChannel();
    }

    // 채널 등록
    public void addChannelByUser(UUID channelId, UUID userId) {
        findUserById(userId).addChannel(channelService.findById(channelId));
    }

    /**
     * 메시지와 연계
     */

}
