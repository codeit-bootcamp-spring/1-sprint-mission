package com.sprint.misson.discordeit.service.basic;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.dto.ChannelDTO;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.ChannelType;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.repository.ChannelRepository;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    @Override
    public Channel create(ChannelType channelType, String name, String description) {
        return channelRepository.save(new Channel(name, channelType, description));
    }

    @Override
    public List<Channel> getChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Channel getChannelByUUID(String channelId) throws CustomException {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return channel;
    }

    @Override
    public List<Channel> getChannelsByName(String channelName) throws CustomException {
        return channelRepository.findAll().stream().filter( c -> c.getChannelName().contains(channelName)).toList();
    }

    @Override
    public List<Channel> getChannelByType(ChannelType channelType) {
        return channelRepository.findAll().stream().filter( c -> c.getChannelType().equals(channelType)).toList();
    }

    @Override
    public Channel updateChannel(String channelId, ChannelDTO channelDTO, long updatedAt) throws CustomException {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
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

        // 상태 변경 처리
        ChannelType newChannelType = channelDTO.getChannelType();
        if (channel.getChannelType() != newChannelType && newChannelType !=null){
            channel.setChannelType(newChannelType);
            isUpdated = true;
        }

        // 변경사항이 있는 경우에만 업데이트 시간 설정
        if (isUpdated) {
            channel.setUpdatedAt(updatedAt);
        }
        return channelRepository.save(channel);
    }

    @Override
    public boolean deleteChannel(Channel channel) throws CustomException {
        Channel ch = channelRepository.findById(channel.getId());

        if (ch == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return channelRepository.delete(ch);
    }

    @Override
    public List<User> getUsersInChannel(Channel channel) throws CustomException {
        Channel ch = channelRepository.findById(channel.getId());
        if (ch == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return ch.getUserList().values().stream().toList();
    }

    @Override
    public boolean addUserToChannel(String channelId, String userId) throws CustomException {
        try {
            //해당 채널이 DB에 존재하는 채널인지 검사
            Channel c = channelRepository.findById(channelId);
            //해당 유저가 DB에 존재하는 유저인지 검사
            User u = userService.getUserByUUID(userId);
            c.getUserList().put(u.getId(), u);
            channelRepository.save(c);
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
        if (channel.getUserList().containsKey(user.getId())) {
            channel.getUserList().remove(user.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserInChannel(Channel channel, User user) {
        return channel.getUserList().containsKey(user.getId());
    }
}
