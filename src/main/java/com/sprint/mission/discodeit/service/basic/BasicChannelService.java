package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository ;
    private final MessageRepository messageRepository;
    //Service
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

    //ReadStatus서비스에서 ReadStatus를 만드는 함수
    private void createReadStatus(Channel channel, ChannelCreateDTO channelCreateDTO) {
        List<User> userList = channelCreateDTO.userList();
        for(User user :userList){
            readStatusService.create(new ReadStatusCreateDTO(channel.getId(), user.getId() ));
        }
    }


    //find

    @Override
    public ChannelFindDTO findDTO(UUID uuid) {
        Channel channel = readChannel(uuid);
        Instant time = readStatusRepository.findLatestTimeByChannelId(uuid);

        //private일 때 userIdList 생성, Public시 null list
        List<UUID> userIdList = null;
        if(channel.getType()==ChannelType.PRIVATE){
            userIdList = userRepository.findAllUserIdByChannelId(uuid);
        }
        ChannelFindDTO channelFindDTO = new ChannelFindDTO(channel, time, userIdList);
        return channelFindDTO;
    }

    //특정 User가 볼 수 있는 Channel 목록을 조회
    @Override
    public List<ChannelFindDTO> findAllByUserId(UUID userId) {
        List<ChannelFindDTO> channelFindDTOList = findAllDTO();
        List<ChannelFindDTO> userChannelFindDTOList = channelFindDTOList.stream()
                .filter(channelFindDTO ->channelFindDTO.getType().equals(ChannelType.PUBLIC)
                &&
                        (channelFindDTO.getType().equals(ChannelType.PRIVATE) &&
                                channelFindDTO.isUserExist(userId)))
                .toList();
        return userChannelFindDTOList;
    }

    @Override
    public List<ChannelFindDTO> findAllDTO() {
        List<Channel> channelList = readAllChannel();
        List<ChannelFindDTO> channelFindDTOList = channelList.stream()
                .map(channel -> findDTO(channel.getId()))
                .collect(Collectors.toList());
        return channelFindDTOList;
    }


    //기존의 read private선언?
    @Override
    public Channel readChannel(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> readAllChannel() {
        List<Channel> channelList = new ArrayList<>(channelRepository.load().values());
        return channelList;
    }


    //update
    @Override
    public Channel update(ChannelUpdateDTO channelUpdateDTO) {
        if (findDTO(channelUpdateDTO.uuid()).getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE  채널은 수정할 수 없습니다.");
        }
        Channel channel = channelRepository.findById(channelUpdateDTO.uuid());
        channel.updateName(channelUpdateDTO.name());
        return channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        //관련된 도메인 삭제
        messageRepository.deleteByChannelId(id);
        readStatusService.deleteByChannelId(id);
        channelRepository.delete(id);
    }
}
