package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final JCFChannelRepository channelRepository;

    public JCFChannelService() {
        this.channelRepository = JCFChannelRepository.getInstance();
    }

    @Override
    public void createChannel(Channel channel) {
        channelRepository.save(channel);
        System.out.println("채널이 등록되었습니다.");

    }

    @Override
    public boolean channelExits(Channel channel) {
        return channelRepository.exists(channel);
    }

    @Override
    public void deleteChannel(Channel channel, User admin) {
        try {
            if (channelExits(channel) && channel.getAdmin().equals(admin)) {
                channel.deleteAllMessage();
                channel.deleteAllMember();
                channelRepository.delete(channel);
                System.out.println("채널이 삭제되었습니다.");
            } else {
                throw new IllegalStateException("채널이 없거나, 관리자가 아닙니다.");
            }
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateChannel(Channel channel, String newName, User admin) {
        try {
            if (channel.getAdmin().equals(admin)) {
                channel.updateChannelName(newName);
                channelRepository.update(channel);
                System.out.println("채널이름이 변경되었습니다.");
            } else {
                throw new IllegalStateException("관리자만 변경할 수 있습니다.");
            }
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> getAllChannel() {
        return channelRepository.load().stream()
                .map(channel -> {
                    Map<String, String> channelInfo = new HashMap<>();
                    channelInfo.put("채널이름", channel.getChannelName());
                    channelInfo.put("관리자", channel.getAdmin().getUserName());
                    return channelInfo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllMemberNames(Channel channel) {
        return channel.getMemberList().stream()
                .map(User::getUserName)
                .collect(Collectors.toList());
    }


}
