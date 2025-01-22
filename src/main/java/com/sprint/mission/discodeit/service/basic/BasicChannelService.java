package com.sprint.mission.discodeit.service.basic;

// 아직 구현 X

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.mockito.internal.matchers.Null;

import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private InputHandler inputHandler;
    private final ChannelRepository channelRepository;

    /**
     * [x ] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.
     * => JCF랑 동일하게 작성했습니다
     * [x ] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.
     * [x ] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)
    **/

    public BasicChannelService(ChannelRepository channelRepository){
        this.channelRepository = channelRepository;
    }

    @Override
    public UUID createChannel(User user, String channelName) {
        Channel channel = new Channel(user, channelName);
        channelRepository.saveChannel(channel);

        return channel.getId();
    }

    @Override
    public int showAllChannels(){
        if (channelRepository.getAllChannels().isEmpty()) {
            System.out.println("No Channels exists.\n");
        }else{
            System.out.println(channelRepository.getAllChannels().toString());
        }
        return channelRepository.getAllChannels().size();
    }

    @Override
    public Channel getChannelById(UUID id) {
        // 특정 채널을 불러오기
        System.out.println(channelRepository.findChannelById(id).toString());
        return channelRepository.findChannelById(id)
                .orElseGet( () -> {
                    System.out.println(" No  " + id.toString() + "  exists.\n");
                    return null;
                });
    }

    @Override
    public void updateChannelName(UUID id) {
        System.out.println("new ChannelName :");
        channelRepository.findChannelById(id).ifPresent( channel -> channel.setChannelName(inputHandler.getNewInput()));
        // 수정 시간 업데이트를 위해
        channelRepository.findChannelById(id).ifPresent(BaseEntity::refreshUpdateAt);
    }

    @Override
    public void deleteAllChannels() {
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            channelRepository.deleteAllChannels();
        }
    }

    @Override
    public void deleteChannelById(UUID id) {
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            channelRepository.deleteChannelById(id);
        }
    }
}
