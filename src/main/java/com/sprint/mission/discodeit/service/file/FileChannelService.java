package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService{
    private final FileChannelRepository channelRepository;

    public FileChannelService() {
        this.channelRepository = FileChannelRepository.getInstance();
    }

    @Override
    public void createChannel(Channel channel) {
        try {
            List<Channel> channels = channelRepository.load();
            if (channels.stream().anyMatch(existingChannel -> existingChannel.getChannelName().equals(channel.getChannelName()))) {
                throw new IllegalArgumentException("이미 등록된 채널입니다.");
            } else {
                channels.add(channel);
                channelRepository.save(channels);
                System.out.println("채널이 등록되었습니다.");
            }
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean channelExits(Channel channel) {
            List<Channel> channels = channelRepository.load();
            return channels.contains(channel);
    }

    @Override
    public void deleteChannel(Channel channel, User admin) {
        try {
            List<Channel> channels = channelRepository.load();
            boolean isRemoved = channels.removeIf(check -> check.equals(channel) && check.getAdmin().equals(admin));
            if (isRemoved) {
                channel.deleteAllMessage();
                channel.deleteAllMember();
                channelRepository.save(channels);
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
            List<Channel> channels = channelRepository.load();
            boolean isAdmin = channels.stream()
                    .filter(check -> check.equals(channel))
                    .anyMatch(check -> check.getAdmin().equals(admin));
            if (isAdmin) {
                channel.setChannelName(newName);
                channelRepository.save(channels);
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
        List<Channel> channels = channelRepository.load();
        return channels.stream()
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
