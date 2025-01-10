package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.log.MyLog;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;


public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelRepository;
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.userService = userService;
        this.channelRepository = new HashMap<>();
    }

    @Override
    public MyLog<Channel>  createChannel(List<User> members, String name, User creator) {
        for (Channel channel : channelRepository.values()) {
            if (channel.getName() == name) {
                return new MyLog<>(null, "이미 존재하는 채널입니다");
            }
        }
        Channel channel = new Channel(members, name, creator);
        channelRepository.put(channel.getId(), channel);
        return new MyLog<>(channel, "채널 생성이 완료되었습니다");
    }

    @Override
    public MyLog<Channel> getChannelByName(String name) {
        for (Channel channel : channelRepository.values()) {
            if (channel.getName() == name) {
                return new MyLog<>(channel, "입력하신 이름에 해당하는 채널을 발견하였습니다");
            }
        }
        return new MyLog<>(null, "입력하신 이름에 해당하는 채널이 존재하지 않습니다");
    }

    @Override
    public MyLog<Channel> updateChannel(String name, User newUser) {//새로운 유저가 채널에 들어갈때
        List<User> users = userService.getAllUser();
        if (!users.contains(newUser)) {
            userService.createUser(newUser.getName(), newUser.getPhone(), newUser.getPhone());
        }
        for (Channel channel : channelRepository.values()) {
            if (channel.getName() == name) {
                channel.update(newUser);
                return new MyLog<>(channel, "신규 회원이 채널에 등록되었습니다");
            }
        }
        return new MyLog<>(null, "해당하는 채널을 찾지 못했습니다");
    }

    @Override
    public List<Channel> getAllChannel() {
        return new ArrayList<>(channelRepository.values());
    }

    @Override
    public MyLog<Channel> deleteChannel(String name) {
        for (Channel channel : channelRepository.values()) {
            if (channel.getName() == name) {
                channelRepository.remove(channel.getId());
                return new MyLog<>(channel, "해당 채널이 삭제되었습니다");
            }
        }
        return new MyLog<>(null, "해당하는 채널이 존재하지 않습니다");
    }

//    @Override
//    public void removeUserFromAllChannels(User user) {
//        for (Channel channel : channelRepository.values()) {
//            channel.removeUser(user);
//        }
//    }
}
