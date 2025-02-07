package com.sprint.mission.service.jcf.main;


import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.ChannelType;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.dto.request.ChannelDto;
import com.sprint.mission.service.dto.response.FindChannelDto;
import com.sprint.mission.service.dto.response.FindPrivateChannelDto;
import com.sprint.mission.service.dto.response.FindPublicChannelDto;
import com.sprint.mission.service.exception.DuplicateName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

    private final JCFChannelRepository channelRepository;
    private final JCFUserService userService;

    //@Override
    public Channel create(ChannelDto dto) {
        // PRIVATE 채널을 생성할 때:
        //[ ] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
        // ????? 채널을 생성했는데 어떻게 바로 User가 있지???
        Channel channel = new Channel(dto.getName(), dto.getDescription(), dto.getChannelType());
        return channelRepository.save(channel);
    }

    //@Override
    public Channel update(ChannelDto dto) {
        Channel updatingChannel = channelRepository.findById(dto.getChannelId());
        updatingChannel.setName(dto.getName());
        updatingChannel.setChannelType(dto.getChannelType());
        updatingChannel.setDescription(dto.getDescription());
        return channelRepository.save(updatingChannel);
    }

    //@Override
    public FindChannelDto findById(UUID id) {
        Channel findChannel = channelRepository.findById(id);
        return getFindChannelDto(findChannel);
    }

    //@Override
    public List<FindChannelDto> findAll() {
        return channelRepository.findAll().stream().map(JCFChannelService::getFindChannelDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static FindChannelDto getFindChannelDto(Channel findChannel) {
        if (findChannel.getChannelType().equals(ChannelType.PRIVATE)){
            return new FindPrivateChannelDto(findChannel);
        } else  {
            return new FindPublicChannelDto(findChannel);
        }
    }


    //@Override
    public void delete(Channel channel) {
        //channel.getUsersImmutable().stream().forEach(user -> user.removeChannel(channel));
        //deletingChannel.removeAllUser();
        channelRepository.delete(channel);
    }

    public void addUser(User user, Channel channel){
        channel.addUser(user);
    }




    public Map<User, Instant> lastReadTimeListInChannel(UUID channelId){
        Channel findChannel = channelRepository.findById(channelId);
        if (findChannel.getChannelType().equals(ChannelType.PUBLIC)){
            return new HashMap<>();
        }

        Map<User, Instant> readTimeMap = new HashMap<>();
        for (User user : findChannel.getUserList()) {
            Instant lastRead = user.getReadStatus().findLastReadByChannel(channelId);
            readTimeMap.put(user, lastRead);
        }
        return readTimeMap;
    }

    /**
     * 중복 검증
     */
    public void validateDuplicateName(String name) {
        boolean isDuplicate = channelRepository.findAll().stream()
                .anyMatch(channel -> channel.getName().equals(name));
        if (isDuplicate) {
            throw new DuplicateName(
                    String.format("%s는 이미 존재하는 이름의 채널명입니다", name));
        }
    }
}

