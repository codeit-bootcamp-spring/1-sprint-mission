package mission.controller.file;

import mission.controller.ChannelController;
import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.service.file.FileChannelService;
import mission.service.file.FileMessageService;
import mission.service.file.FileUserService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

// 처음 : 컨트롤러에서 서로 연관된 메서드를 활용해서 서비스 레이어의 코드를 줄이려함
// 나중 : 나중에 도입할 API 통신을 고려하면 컨트롤러의 각 메서드는 최대한 서로 안 건드는게 나을거라고 판단(단점 : 서비스 계층 코드 증가)
public class FileChannelController implements ChannelController {

    private final FileChannelService fileChannelService = FileChannelService.getInstance();
    private final FileUserService fileUserService = FileUserService.getInstance();
    private final FileMessageService fileMessageService = FileMessageService.getInstance();

    @Override
    public Channel create(String channelName) {
        try {
            return fileChannelService.createOrUpdate(new Channel(channelName));
        } catch (IOException e) {
            throw new RuntimeException("채널 등록 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public Channel updateChannelName(UUID channelId, String newName) {
        return fileChannelService.update(fileChannelService.findById(channelId), newName);
    }

    @Override
    public Channel findById(UUID channelId){
        return fileChannelService.findById(channelId);
    }

    @Override
    public Set<Channel> findAll() {
        return fileChannelService.findAll();
    }

    @Override
    public Channel findChannelByName(String channelName) {
        return fileChannelService.findByName(channelName);
    }

    @Override
    public Set<Channel> findAllChannelByUser(UUID userId) {
        return fileUserService.findById(userId).getChannelsImmutable();
    }

    @Override
    public void delete(UUID channelId) {
        fileMessageService.delete(channelId);
    }

    @Override
    public void addUserByChannel(UUID channelId, UUID userId) {
        fileChannelService.findById(channelId).addUser(fileUserService.findById(userId));
    }

    @Override
    public void drops(UUID channel_Id, UUID droppingUser_Id) {
        fileChannelService.findById(channel_Id).removeUser(fileUserService.findById(droppingUser_Id));
    }

    /**
     * 채널 디렉토리 생성
     */
    public void createChannelDirectory() {
        fileChannelService.createChannelDirect();
    }
}
