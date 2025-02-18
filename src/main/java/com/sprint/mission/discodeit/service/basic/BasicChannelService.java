package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.validation.ChannelValidator;
import com.sprint.mission.discodeit.common.validation.Validator;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.enums.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final Validator<Channel, ChannelDTO.request> channelValidator = new ChannelValidator();
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Long createPublicChannel(ChannelDTO.request channelReqDTO) {
        // 생성
        try {
            ChannelDTO.request channelDto = ChannelDTO.request.builder()
                    .owner(channelReqDTO.owner())
                    .serverName(channelReqDTO.serverName())
                    .description(StringUtils.isEmpty(channelReqDTO.description()) ? "" : channelReqDTO.description())
                    .channelType(channelReqDTO.channelType())
                    .recent(channelReqDTO.recent())
                    .build();

            channelValidator.validateCreate(channelDto);
            return channelRepository.save(new Channel(channelDto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long createPrivateChannel(ChannelDTO.request channelReqDTO) {
        try {
            ChannelDTO.request channelDto = ChannelDTO.request.builder()
                    .owner(channelReqDTO.owner())
                    .serverName(channelReqDTO.serverName().isBlank() ? "" : channelReqDTO.serverName())
                    .description(StringUtils.isEmpty(channelReqDTO.description()) ? "" : channelReqDTO.description())
                    .channelType(channelReqDTO.channelType())
                    .members(channelReqDTO.members())
                    .recent(channelReqDTO.recent())
                    .build();

            channelValidator.validateCreate(channelDto);
            Long channelId = channelRepository.save(new Channel(channelDto));
            Channel channel = channelRepository.load(channelId);

            if (channelReqDTO.members() != null && !channelReqDTO.members().isEmpty()) {
                for (UUID userId : channelReqDTO.members()) {
                    channel.addMember(userId);  // 멤버 추가
                    readStatusRepository.save(new ReadStatus(
                            ReadStatusDTO.request.builder()
                                    .channelId(channel.getId())
                                    .userId(userId)
                                    .lastReadAt(channelDto.recent())
                                    .build()));
                }
            }
            return channelId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChannelDTO.response find(Long id) {
        Channel channel = channelRepository.load(id);
        if (channel == null) throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);

        System.out.println(readStatusRepository.loadAll());

        return ChannelDTO.response.builder()
                .id(id)
                .uuid(channel.getId())
                .ownerId(channel.getOwnerId())
                .serverName(channel.getServerName())
                .description(channel.getDescription())
                .members(channel.getMembers().isEmpty() ? Collections.emptyList() : channel.getMembers())  // UserId 목록 가져옴
                .recentMessageTime(channel.getChannelType() == ChannelType.PUBLIC ? null : readStatusRepository.findUpToDateReadTimeByChannelId(channel.getId()))
                .build();
    }

    @Override
    public ChannelDTO.response find(UUID uuid) {
        return channelRepository.loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst()
                .map(entry -> ChannelDTO.response.builder()
                        .id(entry.getKey())
                        .uuid(entry.getValue().getId())
                        .ownerId(entry.getValue().getOwnerId())
                        .serverName(entry.getValue().getServerName())
                        .description(entry.getValue().getDescription())
                        .members(entry.getValue().getMembers())  // UserId 목록 가져옴
                        .recentMessageTime(entry.getValue().getChannelType() == ChannelType.PUBLIC ? null : readStatusRepository.findUpToDateReadTimeByChannelId(entry.getValue().getId()))
                        .build()
                )
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }


    @Override
    public List<ChannelDTO.response> findAllByUserId(UUID userId) {
        return channelRepository.findChannelsByUserId(userId).entrySet().stream()
                .map(entry -> ChannelDTO.response.builder()
                        .id(entry.getKey())
                        .uuid(entry.getValue().getId())
                        .ownerId(entry.getValue().getOwnerId())
                        .serverName(entry.getValue().getServerName())
                        .description(entry.getValue().getDescription())
                        .members(entry.getValue().getMembers())
                        .recentMessageTime(entry.getValue().getChannelType() == ChannelType.PUBLIC ? null : readStatusRepository.findUpToDateReadTimeByChannelId(entry.getValue().getId()))
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean update(ChannelDTO.update updateDTO) {
        boolean isUpdated = false;
        try {
            Channel channel = channelRepository.load(updateDTO.id());
            if (channel.getChannelType() == ChannelType.PRIVATE) {
                throw new CustomException(ErrorCode.PRIVATE_CANNOT_MODIFY);
            }
            channelRepository.update(updateDTO.id(), channel);
            return isUpdated;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 채널 삭제
    @Override
    public ChannelDTO.response delete(Long id) {
        ChannelDTO.response deleteChannel = find(id);

        messageRepository.deleteAllByChannelId(deleteChannel.uuid());
        readStatusRepository.deleteAllByChannelId(deleteChannel.uuid());

        channelRepository.delete(id);
        return deleteChannel;
    }

    @Override
    public ChannelDTO.response delete(UUID uuid) {
        ChannelDTO.response deleteChannel = find(uuid);

        messageRepository.deleteAllByChannelId(uuid);
        readStatusRepository.deleteAllByChannelId(uuid);

        channelRepository.delete(deleteChannel.id());
        return deleteChannel;
    }
}
