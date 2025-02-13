package com.sprint.mission.service.file;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.file.FileChannelRepository;
import com.sprint.mission.repository.file.FileUserRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.service.exception.DuplicateName;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @SneakyThrows
    @Override
    public Channel create(ChannelDtoForRequest dto) {
        //validateDuplicateName(channel.getName());
        return fileChannelRepository.save(Channel.createChannelByRequestDto(dto));
    }

    @SneakyThrows
    @Override
    public void update(UUID channelId, ChannelDtoForRequest dto) {
        //validateDuplicateName(newName);
        Channel updatingChannel = fileChannelRepository.findById(channelId).orElseThrow(NotFoundId::new);
        updatingChannel.setName(dto.getName());
        updatingChannel.setDescription(dto.getDescription());
        updatingChannel.setChannelType(dto.getChannelType());
        fileChannelRepository.save(updatingChannel);
    }

    @SneakyThrows
    @Override
    public List<FindChannelDto> findAll() {
        List<Channel> channelList = fileChannelRepository.findAll();
        return channelList.stream()
                .map((channel) -> {
                    return channel.getChannelType().equals(ChannelType.PRIVATE)
                            ? new FindPrivateChannelDto(channel)
                            : new FindPublicChannelDto(channel);
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    @SneakyThrows
    @Override
    public FindChannelDto findById(UUID channelId) {
        Channel findChannel = fileChannelRepository.findById(channelId).orElseThrow(NotFoundId::new);
        return findChannel.getChannelType().equals(ChannelType.PUBLIC)
                ? new FindPublicChannelDto(findChannel)
                : new FindPrivateChannelDto(findChannel);
    }

    @SneakyThrows
    @Override
    public void delete(UUID channelId) {
        Channel deletingChannel = fileChannelRepository.findById(channelId).orElseThrow(NotFoundId::new);

        // channel에 참여한 각 user의 channelList 업데이트
        for (User user : deletingChannel.getUserList()) {
            deletingChannel.removeUser(user);
            fileUserRepository.save(user);  // 유저 파일 업데이트
        }

        fileChannelRepository.delete(deletingChannel);
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