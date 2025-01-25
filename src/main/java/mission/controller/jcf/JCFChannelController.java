package mission.controller.jcf;

import mission.controller.ChannelController;
import mission.entity.Channel;
import mission.entity.Message;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;

import java.util.Set;
import java.util.UUID;

public class JCFChannelController implements ChannelController {

    private final JCFUserService userService = JCFUserService.getInstance();
    private final JCFChannelService channelService = JCFChannelService.getInstance();
    private final JCFMessageService messageService = JCFMessageService.getInstance();

    // 채널명은 중복 허용 X
    @Override
    public Channel create(String name) {
        return channelService.createOrUpdate(new Channel(name));
    }

    @Override
    public Channel updateChannelName(UUID channelId, String newName) {
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
        return channelService.findByName(channelName);
    }

    @Override
    public Set<Channel> findAllChannelByUser(UUID userId) {
        return userService.findById(userId).getChannelsImmutable();
    }

    @Override
    public void delete(UUID channelId) {
        channelService.deleteById(channelId);
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
