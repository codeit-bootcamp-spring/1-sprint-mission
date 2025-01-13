package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userData;
    private MessageService messageService;
    private ChannelService channelService;

    //팩토리 패턴으로 인하여 private이면 serviceFactory에서 접근이 불가하므로 public으로 변경
    public JCFUserService(Map<UUID, User> userData) {
        this.userData = userData;
    }

    public void setDependencies(MessageService messageService, ChannelService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
    }

    @Override
    public User createUser(User user){
        if (userData.containsKey(user.getId())) {
            throw new IllegalArgumentException("User ID already exists: " + user.getId());
        }
        userData.put(user.getId(), user);
        System.out.println(user);
        return user;
    }

    @Override
    public Optional<User> readUser(User user) {
        System.out.println(user.toString());
        return Optional.ofNullable(userData.get(user.getId()));
    }

    @Override
    public List<User> readAllUsers() {
        return new ArrayList<>(userData.values());
    }

    @Override
    public User updateUser(User existUser, User updateUser){
        if (!userData.containsKey(existUser.getId())) {
            throw new NoSuchElementException("User not found");
        }
        // 기존 객체의 값을 업데이트
        System.out.println("기존 유저= "+existUser.toString());
        existUser.updateTime();
        existUser.updateUserid(updateUser.getUserid());
        existUser.updateUsername(updateUser.getUsername());
        existUser.updatePassword(updateUser.getPassword());
        existUser.updateUserEmail(updateUser.getEmail());

        userData.put(existUser.getId(), existUser);
        System.out.println("수정 유저= "+existUser.toString());
        return existUser;
    }

    @Override
    public boolean deleteUser(User user){
        if (!userData.containsKey(user.getId())) {
            return false;
        }
        System.out.println(user.toString());

        //사용자 삭제 시 관련 메세지 삭제
        messageService.deleteMessageByUser(user);

        //사용자 삭제 시 모든 채널에서 해당 사용자 삭제
        channelService.readAllChannels().forEach(channel -> {
            if (channel.getParticipants().contains(user)) {
                channel.getParticipants().remove(user);
                System.out.println("채널 '"+ channel.getName() + "'에서 사용자" +
                        "'"+user.getUsername()+ "' 제거 완료");
            }
        });
        userData.remove(user.getId());
        return true;
    }
}