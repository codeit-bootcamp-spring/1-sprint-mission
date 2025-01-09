package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.ChannelEntity;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    // UUID가 중복되지 않는 고유의 값을 보장하기 때문에
    // List를 사용해도 중복되는 항목이 추가되지 않기에 List로 선언하였습니다.

    private final List<ChannelEntity> data = new ArrayList<>();
    // Message Service를 Channel Service에서 이용하기 위해서 의존성 주입
    private MessageService messageService;

    public JCFChannelService (MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void addChannel(ChannelEntity channel) {
        data.add(channel);
    }

    @Override
    public ChannelEntity getChannelById(UUID id) {
        return data.stream().filter(channel -> channel.getId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public ChannelEntity getChannelByIdWithFlg(UUID id) {
        return data.stream().filter(channel -> channel.getId().equals(id) && channel.getChannelDelFlg().equals("0"))
                .findFirst().orElse(null);
    }

    @Override
    public void updateChannelName(UUID id, String channelName) {
        ChannelEntity channel = getChannelById(id);
        if(channel != null){
            channel.updateChannelName(channelName);
            channel.updateUpdatedAt(System.currentTimeMillis());
        }
    }

    @Override
    public void updateChannelDescription(UUID id, String description) {
        ChannelEntity channel = getChannelById(id);
        if(channel != null){
            channel.updateDescription(description);
            channel.updateUpdatedAt(System.currentTimeMillis());
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        // Channel 삭제 시에는 삭제되는 ChannelId 를 가지는 Message 역시 모두 삭제되어야 함.
        if(getChannelById(id) != null){
            data.removeIf(channel -> channel.getId().equals(id));
            System.out.println("Channel 이 삭제되었습니다.");

        } else {
            System.out.println("Channel 삭제 중에 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    @Override
    public void deleteChannelWithFlg(UUID id) {
        ChannelEntity channel = getChannelById(id);
        // Channel 삭제 시에는 삭제되는 ChannelId 를 가지는 Message 역시 모두 삭제되어야 함.
        if(channel != null){
            channel.updateChannelDelFlg("1");
            channel.updateUpdatedAt(System.currentTimeMillis());
            System.out.println("Channel 이 삭제되었습니다.");
        } else {
            System.out.println("Channel 삭제 중에 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }
}
