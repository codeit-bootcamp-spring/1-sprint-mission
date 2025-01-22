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
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(name, type, description);
        String channelId = channel.getId();
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
        Channel ch = getChannels().stream().filter(channel -> channel.getId().equals(channelId)).findFirst().orElse(null);

        if (ch == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return ch;
    }

    @Override
    public List<Channel> getChannelsByName(String channelName) {
        return getChannels().stream().filter(channel -> channel.getChannelName().contains(channelName)).toList();
    }

    @Override
    public List<Channel> getChannelByType(ChannelType channelType) {
        return getChannels().stream().filter(c -> c.getChannelType().equals(channelType)).toList();
    }

    // todo
    // 변경사항 체크 로직 따로 뺄 수 있는지 확인
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

        // 상태 변경 처리
        ChannelType newChannelType = channelDTO.getChannelType();
        if (channel.getChannelType() != newChannelType) {
            channel.setChannelType(newChannelType);
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
        Channel ch = getChannelByUUID(channel.getId());
        if (ch == null) {
            System.out.println("Channel with id " + channel.getId() + " not found");
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return delete(channelDirectory.resolve(ch.getId()));
    }

    @Override
    public List<User> getUsersInChannel(Channel channel) {
        Channel ch = getChannelByUUID(channel.getId());
        if (ch == null) {
            System.out.println("Channel with id " + channel.getId() + " not found");
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return ch.getUserList().values().stream().toList();
    }

    @Override
    public boolean addUserToChannel(String channelId, String userId) throws CustomException {
        try {
            //해당 채널이 DB에 존재하는 채널인지 검사
            Channel c = getChannelByUUID(channelId);
            //해당 유저가 DB에 존재하는 유저인지 검사
            User u = userService.getUserByUUID(userId);
            c.getUserList().put(u.getId(), u);

            Path channelPath = channelDirectory.resolve(c.getId().concat(".ser"));
            save(channelPath, c);
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
            Path channelPath = channelDirectory.resolve(channel.getId().concat(".ser"));
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
