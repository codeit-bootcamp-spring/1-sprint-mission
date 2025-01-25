package mission.controller.file;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.service.file.FileChannelService;
import mission.service.file.FileMessageService;
import mission.service.file.FileUserService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

// 많은 일은 안하지만 그래도 생성, 업데이트 시 id, name, PW 검사정도만
// 이 검사를 뚫어야 (user or channel or message) service가 시작되게 설계
public class FileChannelController {

    private final FileChannelService fileChannelService = FileChannelService.getInstance();

    /**
     * 생성
     */
    public Channel createChannel(String channelName) {
        try {
            return fileChannelService.createOrUpdate(new Channel(channelName));
        } catch (IOException e) {
            throw new RuntimeException("채널 등록 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 수정
     */
    public Channel updateChannelName(UUID channelId, String newName) {
        return fileChannelService.update(fileChannelService.findById(channelId), newName);
    }


    /**
     * 조회
     */
    public Channel findChannelById(UUID channelId){
        return fileChannelService.findById(channelId);
    }

    /**
     * 삭제
     */
    public void deleteChannel(UUID channelId){
        fileChannelService.deleteById(channelId);
    }

    /**
     * 채널 디렉토리 생성
     */

    public void createChannelDirectory() {
        fileChannelService.createChannelDirect();
    }
}
