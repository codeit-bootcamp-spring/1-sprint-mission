package com.srint.mission.discodeit.service.basic;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.repository.ChannelRepository;
import com.srint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    //서비스 로직
    @Override
    public UUID create(String channelName, User channelOwner) {
        Channel channel = new Channel(channelName, channelOwner);
        return channelRepository.save(channel);
    }

    @Override
    public Channel read(UUID id) {
        return channelRepository.findOne(id);
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel updateChannelName(UUID id, User user,String channelName) {
        Channel findChannel = channelRepository.findOne(id);
        if(!findChannel.getChannelOwner().userCompare(user)){
            throw new IllegalStateException("채널 수정 권한이 없습니다.");
        }
        findChannel.setChannelName(channelName);
        channelRepository.save(findChannel);
        return findChannel;
    }

    @Override
    public Channel joinChannel(UUID id, User user) {
        Channel findChannel = channelRepository.findOne(id);
        findChannel.setJoinedUsers(user);
        channelRepository.update(findChannel);
        return findChannel;
    }

    //특정 사용자 채널 탈퇴
    @Override
    public UUID exitChannel(UUID id, User user) {
        Channel findChannel = channelRepository.findOne(id);
        findChannel.deleteJoinedUser(user);
        user.deleteMyChannels(findChannel);
        channelRepository.update(findChannel);
        return findChannel.getId();
    }

    //채널삭제 사용자 권한
    @Override
    public UUID deleteChannel(UUID id, User user) {
        Channel findChannel = channelRepository.findOne(id);
        if(!findChannel.getChannelOwner().userCompare(user)){
            throw new IllegalStateException("채널 삭제 권한이 없습니다.");
        }
        findChannel.getJoinedUsers().forEach(u -> u.deleteMyChannels(findChannel));
        return channelRepository.delete(id);
    }
}
