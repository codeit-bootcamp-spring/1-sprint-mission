package mission.controller.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.service.jcf.JCFChannelService;
import mission.service.jcf.JCFMessageService;
import mission.service.jcf.JCFUserService;

import java.util.Set;
import java.util.UUID;

public class JCFChannelController {

    private final JCFUserService userService = new JCFUserService();
    private final JCFMessageService messageService = new JCFMessageService();
    private final JCFChannelService channelService = new JCFChannelService();

    // 채널명은 중복 허용 X
    public Channel createChannel(String name) {
        return channelService.createOrUpdate(new Channel(name)).or(null);
    }

    // Channel 이름 변경
    public Channel updateChannelName(UUID channelId, String newName) {
        return channelService.update(findChannelById(channelId), newName);
    }

    /**
     * Channel 조회 메서드들
     */
    public Channel findChannelById(UUID id) {
        return channelService.findById(id);
    }

    public Channel findChannelByName(String channelName) {
        return channelService.findByName(channelName);
    }

    public Set<Channel> findAllChannel() {
        return channelService.findAll();
    }

    public Set<Channel> findAllChannelByUser(UUID userId) {
        return userService.findById(userId).getChannelsImmutable();
    }
    /**
     * 삭제
     */
    public void deleteChannel(UUID channelId) {
        channelService.deleteById(channelId);
    }

    /**
     * 유저와의 관계
     */
    // 채널에 유저 등록
    public void addChannelByUser(UUID channelId, UUID userId) {
        findChannelById(channelId).addUser(userService.findById(userId));
        System.out.println("유저 등록 성공");
        // if(updatingChannel != null){
    }

    // 강퇴
    public void drops(UUID channel_Id, UUID droppingUser_Id) {
        findChannelById(channel_Id).removeUser(userService.findById(droppingUser_Id));
    }

    /**
     * 메시지와
     */

    // 채널주인이 삭제 (개인이 삭제하는거는 보류)
    public void deleteMessage(UUID messageId, UUID channelId) {
        Message deletingMessage = findMessageByC_M_Id(channelId, messageId);
        messageService.delete(deletingMessage);
    }



}
