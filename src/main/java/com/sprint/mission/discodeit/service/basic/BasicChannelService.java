package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusService readStatusService;

    @Override
    public Channel createPrivateChannel(ChannelCreateDTO channelCreateDTO, ChannelType type) {
        Channel channel = new Channel(channelCreateDTO, ChannelType.PRIVATE);

        createReadStatus(channel, channelCreateDTO);

        return channelRepository.save(channel);
    }

    @Override
    public Channel createPublicChannel(ChannelCreateDTO channelCreateDTO, ChannelType type) {
        Channel channel = new Channel(channelCreateDTO, ChannelType.PUBLIC);

        createReadStatus(channel, channelCreateDTO);
        return channelRepository.save(channel);
    }

    //ReadStatus서비스에서 ReadStatus를 크리에이트
    private void createReadStatus(Channel channel, ChannelCreateDTO channelCreateDTO) {
        List<User> userList = channelCreateDTO.userList();
        for(User user :userList){
            readStatusService.create(new ReadStatusCreateDTO(channel.getId(), user.getId() ));
        }
    }

    @Override
    public Channel readChannel(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> readAllChannel() {
        List<Channel> channelList = new ArrayList<>(channelRepository.load().values());
        return channelList;
    }

    @Override
    public Channel modifyChannel(UUID id, String name) {
        Channel channel = channelRepository.findById(id);
        channel.updateName(name);
        return channelRepository.save(channel);
        //리포지토리에서 객체를 불러와 이름 변경, 레포지토리에 저장.
    }

    @Override
    public void deleteChannel(UUID id) {
        channelRepository.delete(id);
    }
}
