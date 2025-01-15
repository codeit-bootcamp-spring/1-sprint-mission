package com.sprint.misson.discordeit.service.jcf;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.dto.ChannelDTO;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.ChannelType;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final HashMap<UUID, Channel> data;
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.data = new HashMap<>();
        this.userService = userService;
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
            System.out.println("Channel with id " + channel.getId() + " not found");
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return true;
    }

    @Override
    public List<User> getUsersInChannel(Channel channel) throws RuntimeException {
        Channel ch = data.get(channel.getId());
        if (ch == null) {
            System.out.println("Channel with id " + channel.getId() + " not found");
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return ch.getUserList().values().stream().toList();
    }

    @Override
    public boolean addUserToChannel(Channel channel, User user) throws RuntimeException {
        //유저를 채널에 추가
        try {
            //해당 채널이 DB에 존재하는 채널인지 검사
            Channel c = getChannelByUUID(channel.getId().toString());
            //해당 유저가 DB에 존재하는 유저인지 검사
            User u = userService.getUserByUUID(user.getId().toString());
            c.getUserList().put(u.getId(), u);
            return true;
        } catch (CustomException e) {
            if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
                System.out.println("Failed to add User to this channel. User with id " + user.getId() + " not found");
            } else if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
                System.out.println("Failed to add User to this channel. Channel with id " + channel.getId() + " not found");
            }
        }
        return false;
    }

    @Override
    public boolean deleteUserFromChannel(Channel channel, User user) {
        //유저를 채널에서 삭제
        if (channel.getUserList().containsKey(user.getId())) {
            channel.getUserList().remove(user.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserInChannel(Channel channel, User user) {
        //유저가 채널에 있는지 검사
        //있으면 true, 없으면 false 반환
        return channel.getUserList().containsKey(user.getId());
    }
}
