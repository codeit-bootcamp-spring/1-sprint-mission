package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final HashMap<String, Channel> data;
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.data = new HashMap<>();
        this.userService = userService;
    }

    //생성
    @Override
    public Channel create(CreateChannelDto createChannelDto) {
        Channel channel = new Channel(createChannelDto.channelName(), createChannelDto.channelType(), createChannelDto.channelCategory(), createChannelDto.description());
        data.put(channel.getId(), channel);
        return channel;
    }

    //모두 조회
    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    //단일 조회 - UUID
    @Override
    public Channel findById(String channelId) throws CustomException {
        Channel channel = data.get(channelId);
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND, String.format("Channel with id %s not found", channelId));
        }
        return channel;
    }

    //다건 조회 - 채널 타입
    @Override
    public List<Channel> findByChannelType(ChannelType channelType) {
        return data.values().stream().filter(c -> c.getChannelType().equals(channelType)).toList();
    }

    //다건 조회 - 채널명
    @Override
    public List<Channel> findAllByChannelName(String channelName) {
        return data.values().stream().filter(c -> c.getChannelName().contains(channelName)).toList();
    }

    //수정
    @Override
    public Channel updateChannel(String channelId,UpdateChannelDto updateChannelDto) throws CustomException {
        Channel channel = data.get(channelId);

        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND, String.format("Channel with id %s not found", channelId));
        } else if (updateChannelDto == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA);
        }

        // 변경사항이 있는 경우에만 업데이트 시간 설정
        if (channel.isUpdated(updateChannelDto)) {
            channel.setUpdatedAt(updateChannelDto.updatedAt());
        }
        return channel;
    }

    //삭제
    @Override
    public boolean deleteChannel(Channel channel) throws CustomException {
        return data.remove(channel.getId()) != null;
    }

    @Override
    public List<User> findAllUserInChannel(Channel channel) throws CustomException {
        Channel ch = data.get(channel.getId());
        if (ch == null) {
            System.out.println("Channel with id " + channel.getId() + " not found");
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return ch.getUserList().values().stream().toList();
    }

    @Override
    public boolean addUserToChannel(String channelId, String userId) throws CustomException {
        //유저를 채널에 추가
        try {
            //해당 채널이 DB에 존재하는 채널인지 검사
            Channel c = findById(channelId);
            //해당 유저가 DB에 존재하는 유저인지 검사
            User u = userService.getUserByUUID(userId);
            c.getUserList().put(u.getId(), u);
            return true;
        } catch (CustomException e) {
            if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
                System.out.println("Failed to add User to this channel. User with id " + userId + " not found");
            } else if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
                System.out.println("Failed to add User to this channel. Channel with id " + channelId + " not found");
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
