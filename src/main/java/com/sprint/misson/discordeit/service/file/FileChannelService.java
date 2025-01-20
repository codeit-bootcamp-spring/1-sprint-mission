package com.sprint.misson.discordeit.service.file;

import com.sprint.misson.discordeit.code.ErrorCode;
import com.sprint.misson.discordeit.dto.ChannelDTO;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.ChannelType;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.exception.CustomException;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.UserService;

import java.nio.file.Path;
import java.util.List;

public class FileChannelService extends FileService implements ChannelService {

    private final Path channelDirectory;
    private final UserService userService;

    public FileChannelService(UserService userService) {
        this.userService = userService;
        this.channelDirectory = super.getBaseDirectory().resolve("channel");
        init(channelDirectory);
    }

    @Override
    public Channel createChannel(String name, ChannelType type) {
        Channel channel = new Channel(name, type);
        String channelId = channel.getId().toString();
        //저장
        Path channelPath = channelDirectory.resolve(channelId.concat(".ser"));
        save(channelPath, channel);
        //예외처리?
        return channel;
    }

    @Override
    public List<Channel> getChannels() {
        return load(channelDirectory);
    }

    @Override
    public Channel getChannelByUUID(String channelId) throws CustomException {
        Channel ch = getChannels().stream().filter(channel -> channel.getId().toString().equals(channelId)).findFirst().orElse(null);

        if (ch == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return ch;
    }

    @Override
    public List<Channel> getChannelsByName(String channelName) {
        return getChannels().stream().filter(channel -> channel.getChannelName().toString().contains(channelName)).toList();
    }

    @Override
    public List<Channel> getChannelByType(ChannelType channelType) {
        return getChannels().stream().filter(c -> c.getChannelType().equals(channelType)).toList();
    }

    @Override
    public Channel updateChannel(String channelId, ChannelDTO channelDTO) throws CustomException {
        Channel channel = getChannelByUUID(channelId);

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
            Path channelPath = channelDirectory.resolve(channelId.concat(".ser"));
            save(channelPath, channel);
        }

        return channel;
    }

    @Override
    public boolean deleteChannel(Channel channel) {
        Channel ch = getChannelByUUID(channel.getId().toString());
        if (ch == null) {
            System.out.println("Channel with id " + channel.getId() + " not found");
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return delete(channelDirectory.resolve(ch.getId().toString()));
    }

    @Override
    public List<User> getUsersInChannel(Channel channel) {
        Channel ch = getChannelByUUID(channel.getId().toString());
        if (ch == null) {
            System.out.println("Channel with id " + channel.getId() + " not found");
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return ch.getUserList().values().stream().toList();
    }

    @Override
    public boolean addUserToChannel(Channel channel, User user) throws CustomException {
        try {
            //해당 채널이 DB에 존재하는 채널인지 검사
            Channel c = getChannelByUUID(channel.getId().toString());
            //해당 유저가 DB에 존재하는 유저인지 검사
            User u = userService.getUserByUUID(user.getId().toString());
            c.getUserList().put(u.getId(), u);

            Path channelPath = channelDirectory.resolve(c.getId().toString().concat(".ser"));
            save(channelPath, c);
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
        if (channel.getUserList().containsKey(user.getId())) {
            channel.getUserList().remove(user.getId());
            Path channelPath = channelDirectory.resolve(channel.getId().toString().concat(".ser"));
            save(channelPath, channel);
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserInChannel(Channel channel, User user) {
        return channel.getUserList().containsKey(user.getId());
    }
}
