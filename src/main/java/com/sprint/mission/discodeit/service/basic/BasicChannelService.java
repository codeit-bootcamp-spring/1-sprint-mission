package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.validation.Validator;
import com.sprint.mission.discodeit.common.validation.ValidatorImpl;
import com.sprint.mission.discodeit.dto.ChannelReqDTO;
import com.sprint.mission.discodeit.dto.ChannelResDTO;
import com.sprint.mission.discodeit.dto.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicChannelService implements ChannelService {
    private final Validator validator = new ValidatorImpl();
    private ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Long createChannel(User owner, String serverName, String description, String iconImgPath) {
        // DTO에 user 객체 + 필요 정보 넣어서 전달되면
        // 생성
        try {
            if (owner == null) {
                throw new CustomException(ErrorCode.OWNER_CANNOT_BLANK);
            }

            if (serverName == null) {
                throw new CustomException(ErrorCode.SERVERNAME_CANNOT_BLANK);
            }

            description = StringUtils.isEmpty(description) ? "" : description;
            iconImgPath = StringUtils.isEmpty(iconImgPath) ? "defaultSeverIcon.png" : iconImgPath;

            Channel channel = new Channel(new ChannelReqDTO(
                    owner, serverName, description, iconImgPath
            ));
            Long channelId = channelRepository.saveChannel(channel);
            return channelId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChannelResDTO getChannel(Long id) {
        Channel channel = findChannelById(id);
        if (channel == null) throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        return new ChannelResDTO(id, channel, channel.getOwner());
    }

    // TODO: 'Optional. get()' without 'isPresent()' check <- 확인
    @Override
    public ChannelResDTO getChannel(String uuid) {
        Map<Long, Channel> allChannels = channelRepository.loadAllChannels();
        return allChannels.entrySet().stream()
                .filter(entry -> entry.getValue().getId().toString().equals(uuid))
                .findFirst()
                .map(entry -> new ChannelResDTO(entry.getKey(), entry.getValue(), entry.getValue().getOwner()))
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }


    @Override
    public List<ChannelResDTO> getAllChannel() {
        return channelRepository.loadAllChannels().entrySet().stream()
                .map(entry ->
                        new ChannelResDTO(entry.getKey(), entry.getValue(), entry.getValue().getOwner()))
                .collect(Collectors.toList());
    }

    @Override
    public Channel findChannelById(Long id) {
        return channelRepository.loadChannel(id);
    }

    @Override
    public Optional<Map.Entry<Long, Channel>> findChannelByUUID(UUID uuid) {
        Map<Long, Channel> allChannels = channelRepository.loadAllChannels();
        return allChannels.entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst();
    }

    @Override
    public boolean updateChannelInfo(Long id, ChannelUpdateDTO updateInfo) {
        boolean isUpdated = false;
        try {
            Channel channel = findChannelById(id);
            if (updateInfo.getOwner() != null && !channel.getOwner().getUserName().getName().equals(updateInfo.getOwner().getUserName().getName())) {
                channel.updateOwner(updateInfo.getOwner());
                isUpdated = true;
            }

            if (updateInfo.getServerName() != null && !channel.getServerName().getName().equals(updateInfo.getServerName())) {
                channel.updateServerName(updateInfo.getServerName());
                isUpdated = true;
            }

            if (updateInfo.getDescription() != null && !channel.getDescription().equals(updateInfo.getDescription())) {
                channel.updateDescription(updateInfo.getDescription());
                isUpdated = true;
            }

            if (updateInfo.getIconImgPath() != null && !channel.getIconImgPath().equals(updateInfo.getIconImgPath())) {
                channel.updateIconImgPath(updateInfo.getIconImgPath());
                isUpdated = true;
            }
            channelRepository.updateChannel(id, channel);
            return isUpdated;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 채널 삭제
    @Override
    public ChannelResDTO deleteChannel(Long id) {
        ChannelResDTO deleteChannel = getChannel(id);
        channelRepository.deleteChannel(id);
        return deleteChannel;
    }

    @Override
    public ChannelResDTO deleteChannel(String uuid) {
        ChannelResDTO deleteChannel = getChannel(uuid);
        channelRepository.deleteChannel(deleteChannel.getId());
        return deleteChannel;
    }

}
