package com.sprint.mission.service.jcf.main;


import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.dto.request.ChannelDto;
import com.sprint.mission.service.dto.response.FindChannelDto;
import com.sprint.mission.service.dto.response.FindPrivateChannelDto;
import com.sprint.mission.service.dto.response.FindPublicChannelDto;
import com.sprint.mission.service.dto.response.FindUserDto;
import com.sprint.mission.service.exception.DuplicateName;
import com.sprint.mission.service.exception.NotFoundId;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

    private final JCFChannelRepository channelRepository;
    private final JCFUserService userService;
    private final JCFUserRepository userRepository;
    private final UserStatusService userStatusService;

    //@Override
    public Channel create(ChannelDto dto) {
        // PRIVATE 채널을 생성할 때:
        // [] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
        // ????? 채널을 생성했는데 어떻게 바로 User가 있지???
        Channel channel = new Channel(dto.getName(), dto.getDescription(), dto.getChannelType());
        return channelRepository.save(channel);
    }

    //@Override
    public void update(ChannelDto dto) {
        channelRepository.findById(dto.getChannelId())
                .map((updatingChannel) -> {
                    updatingChannel.setName(dto.getName());
                    updatingChannel.setChannelType(dto.getChannelType());
                    updatingChannel.setDescription(dto.getDescription());
                    log.info("{} 수정", updatingChannel);
                    return channelRepository.save(updatingChannel);
                })
                .orElseThrow(NotFoundId::new);
    }

    //@Override
    public FindChannelDto findById(UUID id) {
        return channelRepository.findById(id)
                .map((findChannel) -> getFindChannelDto(findChannel))
                .orElse(new FindPublicChannelDto());
    }

    //@Override
    public List<FindChannelDto> findAll() {
        return channelRepository.findAll().stream()
                .map((channel) -> getFindChannelDto(channel))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * [ ] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId
     * [ ] PUBLIC 채널 목록은 전체 조회합니다.
     * [ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.
     */
    public List<FindChannelDto> findAllByUserId(UUID userId) {
        ArrayList<FindChannelDto> findChannelListDto = channelRepository.findAll().stream()
                .filter((channel) -> channel.getChannelType().equals(ChannelType.PUBLIC))
                .map((channel) -> getFindChannelDto(channel))
                .collect(Collectors.toCollection(ArrayList::new));

        userRepository.findById(userId).ifPresent((user) -> {
            user.getChannels().stream()
                    .filter((channel -> channel.getChannelType().equals(ChannelType.PRIVATE)))
                    .forEach((joiningPrivateChannel) -> findChannelListDto.add(new FindPrivateChannelDto(joiningPrivateChannel)));
        });
        return findChannelListDto;
    }

    private FindChannelDto getFindChannelDto(Channel findChannel) {
        return (findChannel.getChannelType().equals(ChannelType.PRIVATE)
                ? new FindPrivateChannelDto(findChannel)
                : new FindPublicChannelDto(findChannel));
    }


    //@Override
    public void delete(Channel channel) {
        //channel.getUsersImmutable().stream().forEach(user -> user.removeChannel(channel));
        //deletingChannel.removeAllUser();
        channelRepository.delete(channel);
    }

    public void addUser(User user, Channel channel) {
        channel.addUser(user);
    }


    public Map<FindUserDto, Instant> lastReadTimeListInChannel(UUID channelId) {
        List<User> userList = channelRepository.findById(channelId)
                .filter((findChannel) -> !findChannel.getChannelType().equals(ChannelType.PUBLIC))
                .map(Channel::getUserList)
                .orElse(new ArrayList<>());

        // 유저별 이 채널 마지막 readTime
        Map<FindUserDto, Instant> readTimeMap = new HashMap<>();
        for (User user : userList) {
            userStatusService.findById(user.getId()).ifPresentOrElse((userStatus) -> {
                readTimeMap.put(new FindUserDto(user, userStatus.isOnline()), user.getReadStatus().findLastReadByChannel(channelId));
            }, () -> readTimeMap.put(new FindUserDto(user, false), null));
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

