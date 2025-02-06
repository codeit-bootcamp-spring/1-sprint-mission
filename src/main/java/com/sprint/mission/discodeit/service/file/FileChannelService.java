package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

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
    public Channel create(CreateChannelDto createChannelDto) {
        Channel channel = new Channel(createChannelDto.channelName(), createChannelDto.channelType(), createChannelDto.channelCategory(), createChannelDto.description());
        String channelId = channel.getId();
        //저장
        Path channelPath = channelDirectory.resolve(channelId.concat(".ser"));
        save(channelPath, channel);
        //예외처리?
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return load(channelDirectory);
    }

    @Override
    public Channel findById(String channelId) throws CustomException {
        Channel ch = findAll().stream().filter(channel -> channel.getId().equals(channelId)).findFirst().orElse(null);

        if (ch == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return ch;
    }

    @Override
    public List<Channel> findAllByChannelName(String channelName) {
        return findAll().stream().filter(channel -> channel.getChannelName().contains(channelName)).toList();
    }

    @Override
    public List<Channel> findByChannelType(ChannelType channelType) {
        return findAll().stream().filter(c -> c.getChannelType().equals(channelType)).toList();
    }

    @Override
    public Channel updateChannel(String channelId, UpdateChannelDto updateChannelDto) throws CustomException {
        Channel channel = findById(channelId);

        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND, String.format("Channel with id %s not found", channelId));
        } else if (updateChannelDto == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA);
        }

        // 변경사항이 있는 경우에만 업데이트 시간 설정
        if (channel.isUpdated(updateChannelDto)) {
            channel.setUpdatedAt(updateChannelDto.updatedAt());
            Path channelPath = channelDirectory.resolve(channelId.concat(".ser"));
            save(channelPath, channel);
        }

        return channel;
    }

    @Override
    public boolean deleteChannel(Channel channel) {
        Channel ch = findById(channel.getId());
        if (ch == null) {
            System.out.println("Channel with id " + channel.getId() + " not found");
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }
        return delete(channelDirectory.resolve(ch.getId()));
    }

    @Override
    public List<User> findAllUserInChannel(Channel channel) {
        Channel ch = findById(channel.getId());
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
            Channel c = findById(channelId);
            //해당 유저가 DB에 존재하는 유저인지 검사
            User u = userService.findById(userId);
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
