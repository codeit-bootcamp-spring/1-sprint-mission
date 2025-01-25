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
        channelService.validateDuplicateName(name);
        return channelService.createOrUpdate(new Channel(name));
    }

    // Channel 이름 변경
    public Channel updateChannelName(UUID channelId, String newName) {
        Channel updatingChannel = findChannelById(channelId);
        return channelService.update(updatingChannel, newName);
    }


    // Channel 찾는 것들
    public Channel findChannelById(UUID id) {
        return channelService.findById(id);
    }

    public Channel findChannelByName(String channelName) {
        return channelService.findByName(channelName);
    }

    public Set<Channel> findAllChannel() {
        return channelService.findAll();
    }

    public Set<Channel> findAllChannelByUser(UUID userId){
        return findUserById(userId).getChannelsImmutable();
    }

    // 채널 가입
    public void addChannelByUser(UUID channelId, UUID userId) {
        Channel channel = findChannelById(channelId);
        findUserById(userId).addChannel(channel); // 양방향으로 더하는 로직
    }

    // 채널주인이 삭제 (개인이 삭제하는거는 보류)
    public void deleteMessage(UUID messageId, UUID channelId) {
        Message deletingMessage = findMessageByC_M_Id(channelId, messageId);
        messageService.delete(deletingMessage);
    }

    // 강퇴 및 채널 탈퇴
    public void drops(UUID channel_Id, UUID droppingUser_Id) {
        Channel droppingChannel = findChannelById(channel_Id);
        User droppingUser = findUserById(droppingUser_Id);
        droppingUser.removeChannel(droppingChannel);
    }

    public void deleteChannel(UUID channelId) {
        channelService.deleteById(channelId);
    }


}
