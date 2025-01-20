package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.common.validation.Validator;
import com.sprint.mission.discodeit.common.validation.ValidatorImpl;
import com.sprint.mission.discodeit.dto.ChannelReqDTO;
import com.sprint.mission.discodeit.dto.ChannelResDTO;
import com.sprint.mission.discodeit.dto.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.ChannelService;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {

    private final Validator validator = new ValidatorImpl();
    private final Map<Long, Channel> channelData;
    private final AtomicLong idGenerator;

    public JCFChannelService() {
        this.channelData = new HashMap<>();
        this.idGenerator = new AtomicLong(1);
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
            Long id = idGenerator.getAndIncrement();
            channelData.put(id, channel);
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChannelResDTO getChannel(Long id) {
        Channel channel = Objects.requireNonNull(findChannelById(id), "해당 ID의 채널이 존재하지 않습니다.");
        return new ChannelResDTO(id, channel, channel.getOwner());
    }

    // TODO: 'Optional. get()' without 'isPresent()' check <- 확인
    @Override
    public ChannelResDTO getChannel(String uuid) {
        Optional<Map.Entry<Long, Channel>> channel = Objects.requireNonNull(findChannelByUUID(UUID.fromString(uuid)), "해당 ID의 채널이 존재하지 않습니다.");
        return new ChannelResDTO(channel.get().getKey(), channel.get().getValue(), channel.get().getValue().getOwner());
    }

    @Override
    public Channel getChannelToChannelObj(Long id) {
        return Objects.requireNonNull(findChannelById(id), "해당 ID의 채널이 존재하지 않습니다.");
    }

    @Override
    public List<ChannelResDTO> getAllChannel() {
        return channelData.entrySet().stream()
                .map(entry ->
                        new ChannelResDTO(entry.getKey(), entry.getValue(), entry.getValue().getOwner()))
                .collect(Collectors.toList());
    }

    @Override
    public Channel findChannelById(Long id) {
        return channelData.get(id);
    }

    @Override
    public Optional<Map.Entry<Long, Channel>> findChannelByUUID(UUID uuid) {
        return channelData.entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst();
    }

    // 채널 제목 수정
    // 채널 소개 수정
    // 채널 이미지 수정
    // 채널 주인 양도 (소유권 이전)
    @Override
    public boolean updateChannelInfo(Long id, ChannelUpdateDTO updateInfo) {
        boolean isUpdated = false;
        try {
            Channel channel = getChannelToChannelObj(id);
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
            channelData.put(id, channel);
            return isUpdated;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 채널 삭제
    @Override
    public ChannelResDTO deleteChannel(Long id) {
        ChannelResDTO deleteChannel = getChannel(id);
        channelData.remove(deleteChannel.getId());
        return deleteChannel;
    }

    @Override
    public ChannelResDTO deleteChannel(String uuid) {
        ChannelResDTO deleteChannel = getChannel(uuid);
        channelData.remove(deleteChannel.getId());
        return deleteChannel;
    }
}
