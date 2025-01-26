package mission.controller.file;

import mission.controller.ChannelController;
import mission.entity.Channel;
import mission.service.file.FileChannelService;
import mission.service.file.FileMessageService;
import mission.service.file.FileUserService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Channel findChannel = fileChannelService.findAll().stream().collect(Collectors.toMap(
                Channel::getName, Function.identity())).get(channelName);

        if (findChannel == null) {
            System.out.printf("%s는 없는 채널명입니다.", channelName);
            System.out.println();
            return null;
        } else {
            return findChannel;
        }
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
    @Override
    public void createChannelDirectory() {
        fileChannelService.createChannelDirect();
    }
}
