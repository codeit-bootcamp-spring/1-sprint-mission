package com.sprint.mission.service.jcf.main;


import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.dto.response.FindUserDto;
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

    @Override
    public void create(ChannelDtoForRequest requestDTO) {
        /**
         * 요구사항 : PRIVATE 채널을 생성할 때, 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
         * 의문점 : ????? 채널을 생성했는데 어떻게 바로 User가 있지???
         * 이런 부분 떔에 PUBLIC-PRIVATE 분리해야 될 이유를 찾지 못했다.
         */
        Channel createdChannel = Channel.createChannelByRequestDto(requestDTO);
        channelRepository.save(createdChannel);
    }

    @Override
    public void update(UUID channelId, ChannelDtoForRequest dto) {
        Channel updatingChannel = channelRepository.findById(channelId).orElseThrow(NotFoundId::new);
        updatingChannel.updateByDTO(dto);
        channelRepository.save(updatingChannel);
    }

    @Override
    public Channel findById(UUID channelId) {
        return channelRepository.findById(channelId).orElseThrow(NotFoundId::new);

    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    /**
     * [ ] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId
     * [ ] PUBLIC 채널 목록은 전체 조회합니다.
     * [ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.
     */
    public List<Channel> findAllByUserId(UUID userId) {

        // 1. Public 채널
        List<Channel> findChannelList = channelRepository.findAll().stream()
                .filter((channel) -> channel.getChannelType().equals(ChannelType.PUBLIC))
                .collect(Collectors.toCollection(ArrayList::new));

        // 2. USER가 참여한 PRIVATE 채널을 1번에 더하기
        userRepository.findById(userId).ifPresent((user) -> {
            user.getChannels().stream()
                    .filter((channel -> channel.getChannelType().equals(ChannelType.PRIVATE)));
        });
        return findChannelList;
    }

    @Override
    public void delete(UUID channelId) {
        Channel deletingChannel = channelRepository.findById(channelId).orElseThrow(NotFoundId::new);
        deletingChannel.getUserList().forEach(user -> {
                    user.getChannels().remove(deletingChannel);
                    userRepository.save(user);
                    // JPA 쓰고 싶다....
                });

        channelRepository.delete(channelId);
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

    /**
     * 응답 DTO (타입별)
     */
    private FindChannelDto getFindChannelDto(Channel findedChannel) {
        return (findedChannel.getChannelType().equals(ChannelType.PRIVATE)
                ? new FindPrivateChannelDto(findedChannel)
                : new FindPublicChannelDto(findedChannel));
    }

}

