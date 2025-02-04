package mission.controller.jcf;

import mission.controller.ChannelController;
import mission.entity.Channel;
import mission.entity.Message;
import mission.service.UserService;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


public class JCFChannelController implements ChannelController {

    private final JCFUserService userService = JCFUserService.getInstance();
    private final JCFChannelService channelService = JCFChannelService.getInstance();

    // 채널명은 중복 허용 X
    @Override
    public Channel create(String name) {
        channelService.validateDuplicateName(name);
        return channelService.createOrUpdate(new Channel(name));
    }

    @Override
    public Channel updateChannelName(UUID channelId, String newName) {
        channelService.validateDuplicateName(newName);
        return channelService.update(findById(channelId), newName);
    }

    @Override
    public Channel findById(UUID id) {
        return channelService.findById(id);
    }

    @Override
    public Set<Channel> findAll() {
        return channelService.findAll();
    }

    @Override
    public Channel findChannelByName(String channelName) {
        // 변환후 조회 vs filter => 어떤게 빠를까
        Channel findChannel = channelService.findAll().stream()
                .collect(Collectors.toMap(
                        Channel::getName,
                        Function.identity()
                ))
                .get(channelName);
        if (findChannel == null) {
            throw new IllegalArgumentException(String.format(
                    "%s는 없는 채널명입니다.", channelName));
        }
        return findChannel;
    }

    @Override
    public Set<Channel> findAllChannelByUser(UUID userId) {
        return userService.findById(userId).getChannelsImmutable();
    }

    @Override
    public void delete(UUID channelId) {
        channelService.delete(channelService.findById(channelId));
    }

    /**
     * 유저와의 관계
     */
    // 채널에 유저 등록
    @Override
    public void addUserByChannel(UUID channelId, UUID userId) {
        findById(channelId).addUser(userService.findById(userId));
        System.out.println("유저 등록 성공");
        // if(updatingChannel != null){
    }

    // 강퇴
    @Override
    public void drops(UUID channel_Id, UUID droppingUser_Id) {
        findById(channel_Id).removeUser(userService.findById(droppingUser_Id));
    }

    @Override
    public void createChannelDirectory() {}

    /**
     * 메시지와
     */
//
//    // 채널주인이 삭제 (개인이 삭제하는거는 보류)
//    public void deleteMessage(UUID messageId, UUID channelId) {
//
//    }
//


}
