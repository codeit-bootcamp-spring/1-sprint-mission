package com.sprint.mission.service.file;

import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.file.main.FileChannelRepository;
import com.sprint.mission.repository.file.main.FileUserRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.service.exception.DuplicateName;
import com.sprint.mission.service.exception.NotFoundId;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileChannelService implements ChannelService {

    private final FileChannelRepository fileChannelRepository;
    private final FileUserService fileUserService;
    private final FileUserRepository fileUserRepository;
    private final FileMessageService fileMessageService;
    private final UserStatusService userStatusService;

    @Override
    public void create(ChannelDtoForRequest dto) {
        //validateDuplicateName(channel.getName());
        fileChannelRepository.save(Channel.createChannelByRequestDto(dto));
    }

    @Override
    public void update(UUID channelId, ChannelDtoForRequest dto) {
        //validateDuplicateName(newName);
        Channel updatingChannel = fileChannelRepository.findById(channelId).orElseThrow(NotFoundId::new);
        updatingChannel.updateByDTO(dto);
        fileChannelRepository.save(updatingChannel);
    }

    @Override
    public Channel findById(UUID channelId) {
        return fileChannelRepository.findById(channelId).orElseThrow(NotFoundId::new);
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        // 1. Public 채널
        List<Channel> findChannelList = fileChannelRepository.findAll().stream()
                .filter((channel) -> channel.getChannelType().equals(ChannelType.PUBLIC))
                .collect(Collectors.toCollection(ArrayList::new));

        // 2. USER가 참여한 PRIVATE 채널을 1번에 더하기
        fileUserRepository.findById(userId).ifPresent((user) -> {
            user.getChannels().stream()
                    .filter((channel -> channel.getChannelType().equals(ChannelType.PRIVATE)));
        });
        return findChannelList;
    }

    @Override
    public List<Channel> findAll() {
        return fileChannelRepository.findAll();
    }

    @Override
    public void delete(UUID channelId) {
        Channel deletingChannel = fileChannelRepository.findById(channelId).orElseThrow(NotFoundId::new);

        deletingChannel.getMessageList().forEach((message) -> {
            fileMessageService.delete(message.getId());
        });

        deletingChannel.getUserList().forEach(user -> {
            user.getChannels().remove(deletingChannel);
            // JPA가 아니므로 user의 수정사항을 다시 저장하기
            fileUserRepository.save(user);
        });

        fileChannelRepository.delete(channelId);
    }

    public Map<FindUserDto, Instant> lastReadTimeListInChannel(UUID channelId) {
        Channel inChannel = fileChannelRepository.findById(channelId).orElseThrow(NotFoundId::new);
        if (inChannel.getChannelType().equals(ChannelType.PUBLIC)) {
            throw new RuntimeException("Public 채널은 유저별 lastReadTime 호출 불가");
        }

        // 유저별 이 채널 마지막 readTime
        // 이 때 쓰는 findUserDto는 profile null
        Map<FindUserDto, Instant> readTimeMap = new HashMap<>();
        for (User user : inChannel.getUserList()) {
            UserStatus status = userStatusService.findById(user.getId());
            readTimeMap.put(new FindUserDto(user, status.isOnline()), user.getReadStatus().findLastReadByChannel(channelId));
        }
        return readTimeMap;
    }


    /**
     * 검증
     */
    public void validateDuplicateName(String validatingName) {
        Map<String, Channel> channelMap = fileChannelRepository.findAll().stream().collect(Collectors.toMap(
                Channel::getName,
                Function.identity()
        ));
        if (!(channelMap.get(validatingName) == null)) {
            throw new DuplicateName("채널명 중복");
        }
    }

    /**
     * 세팅
     */

    @SneakyThrows
    public void createChannelDirect() {
        fileChannelRepository.createChannelDirectory();
    }
}