package com.sprint.misson.discordeit.service.jcf;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.dto.ChannelDTO;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.ChannelType;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private static final JCFChannelService instance = new JCFChannelService();
    private final HashMap<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    public static JCFChannelService getInstance() {
        return instance;
    }

    //생성
    @Override
    public Channel CreateChannel(String name, ChannelType type) {
        Channel channel = new Channel(name, type);
        data.put(channel.getId(), channel);
        return channel;
    }

    //모두 조회
    @Override
    public List<Channel> getChannels() {
        return new ArrayList<>(data.values());
    }

    //단일 조회 - UUID
    @Override
    public Channel getChannelByUUID(String channelId) throws RuntimeException {
        Channel channel = data.get(UUID.fromString(channelId));
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND, String.format("Channel with id %s not found", channelId));
        }
        return channel;
    }

    //다건 조회 - 채널 타입
    @Override
    public List<Channel> getChannelByType(ChannelType channelType) {
        return data.values().stream().filter(c -> c.getChannelType().equals(channelType)).toList();
    }

    //다건 조회 - 채널명
    @Override
    public List<Channel> getChannelsByName(String channelName) {
        return data.values().stream().filter(c -> c.getChannelName().contains(channelName)).toList();
    }

    //수정
    @Override
    public Channel updateChannel(String channelId, ChannelDTO channelDTO) throws RuntimeException {
        Channel channel = data.get(UUID.fromString(channelId));

        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND, String.format("Channel with id %s not found", channelId));
        } else if (channelDTO == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA);
        }

        //변경 여부 체크
        boolean isUpdated = false;

        // 채널 이름 변경 처리
        String newChannelName = channelDTO.getChannelName();
        if (newChannelName != null && !newChannelName.isEmpty() && !newChannelName.equals(channel.getChannelName())) {
            channel.setChannelName(newChannelName);
            isUpdated = true;
        }

        // hidden 상태 변경 처리
        boolean newHiddenStatus = channelDTO.isHidden();
        if (channel.isHidden() != newHiddenStatus) {
            channel.setHidden(newHiddenStatus);
            isUpdated = true;
        }

        // 변경사항이 있는 경우에만 업데이트 시간 설정
        if (isUpdated) {
            channel.setUpdatedAt();
        }
        return channel;
    }

    //삭제
    @Override
    public boolean deleteChannel(Channel channel) throws RuntimeException {
        Channel ch = data.get(channel.getId());
        if (ch == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND, String.format("Channel with id %s not found", channel.getId()));
        }
        return true;
    }
}
