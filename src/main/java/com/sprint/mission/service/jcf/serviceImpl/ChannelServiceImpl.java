package com.sprint.mission.service.jcf.serviceImpl;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.ChannelMapper;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;

import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.entity.main.ChannelType.PRIVATE;
import static com.sprint.mission.entity.main.ChannelType.PUBLIC;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageService messageService;
    private final ChannelMapper channelMapper;
    private final ReadStatusService readStatusService;


    @Override
    public Channel createPublicChannel(PublicChannelCreateDTO request) {
        return channelRepository.save(channelMapper.toPublicEntity(request, PUBLIC));
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateDTO request) {
        Channel createdChannel = channelRepository.save(channelMapper.toPrivateEntity(PRIVATE));
        List<UUID> userIdList = request.participantIds();
        userIdList.forEach(userId -> {
            ReadStatusCreateRequest readStatusCreateDTO = new ReadStatusCreateRequest(userId, createdChannel.getId(), createdChannel.getCreatedAt());
            readStatusService.create(readStatusCreateDTO);
        });
        return createdChannel;
    }

    @Transactional(readOnly = true)
    @Override
    public Channel findById(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {

        // Private 채널에 대한 DTO 변환 - 나중에 mapper 수정 후 람다식 적용 ㄱ
        List<ChannelDto> privateChannelDTOList = channelRepository.findAllPrivateChannelByUserId(userId);

        // Public 채널에 대한 DTO 변환
        List<ChannelDto> publicChannelList = channelRepository.findAllByChannelType(PUBLIC).stream()
                .map(channelMapper::toDto)
                .toList();

        // 합치기
        List<ChannelDto> channelDtoList = new ArrayList<>();
        channelDtoList.addAll(privateChannelDTOList);
        channelDtoList.addAll(publicChannelList);

        return channelDtoList;
    }

    @Override
    public Channel update(UUID channelId, ChannelDtoForUpdate dto) {
        Channel updatingChannel = this.findById(channelId);
        if (updatingChannel.isPrivate()) {
            throw new CustomException(ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL);
        }
        updatingChannel.update(dto.name(), dto.description());
        return updatingChannel;
    }

    @Override
    public void delete(UUID channelId) {
        Channel deletingChannel = this.findById(channelId);

        if (deletingChannel.isPrivate()) {
            readStatusRepository.deleteAllByChannel(deletingChannel);
        }
        messageService.deleteAllByChannelId(channelId);
        channelRepository.delete(deletingChannel);
    }
}

