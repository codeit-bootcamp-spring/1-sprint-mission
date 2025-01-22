package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;


public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;


    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(Channel channel) {
        List<Channel> channels = channelRepository.loadAll(); // 저장소에서 모든 채널 로드
        channels.add(channel); // 새로운 채널 추가
        channelRepository.saveAll(channels); // 저장소에 저장
        System.out.println("채널이 생성되었습니다: " + channel);
    }

    @Override
    public Channel readChannel(String id) {
        List<Channel> channels = channelRepository.loadAll(); // 저장소에서 모든 채널 로드
        return channels.stream()
                .filter(channel -> channel.getId().toString().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + id));
    }

    @Override
    public List<Channel> readAllChannel() {
        return channelRepository.loadAll(); // 저장소에서 모든 채널 로드
    }

    @Override
    public void updateChannel(Channel channel) {
        List<Channel> channels = channelRepository.loadAll(); // 저장소에서 모든 채널 로드
        boolean updated = false;
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getId().equals(channel.getId())) {
                channels.set(i, channel); // 채널 업데이트
                updated = true;
                break;
            }
        }
        if (!updated) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + channel.getId());
        }
        channelRepository.saveAll(channels); // 저장소에 저장
        System.out.println("채널이 업데이트되었습니다: " + channel);
    }

    @Override
    public void deleteChannel(String id) {
        List<Channel> channels = channelRepository.loadAll(); // 저장소에서 모든 채널 로드
        if (!channels.removeIf(channel -> channel.getId().toString().equals(id))) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + id);
        }
        channelRepository.saveAll(channels); // 저장소에 저장
        System.out.println("채널이 삭제되었습니다: " + id);
    }

    @Override
    public void addMember(String channelId, User member) {
        List<Channel> channels = channelRepository.loadAll(); // 저장소에서 모든 채널 로드
        Channel channel = channels.stream()
                .filter(c -> c.getId().toString().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));

        if (!channel.getMembers().contains(member)) {
            channel.getMembers().add(member); // 멤버 추가
            channelRepository.saveAll(channels); // 변경된 채널 저장
            System.out.println("멤버가 추가되었습니다: " + member.getName());
        } else {
            System.out.println("이미 채널에 존재하는 멤버입니다: " + member.getName());
        }
    }

    @Override
    public void removeMember(String channelId, User member) {
        List<Channel> channels = channelRepository.loadAll(); // 저장소에서 모든 채널 로드
        Channel channel = channels.stream()
                .filter(c -> c.getId().toString().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));

        if (channel.getMembers().remove(member)) {
            channelRepository.saveAll(channels); // 변경된 채널 저장
            System.out.println("멤버가 삭제되었습니다: " + member.getName());
        } else {
            System.out.println("채널에 존재하지 않는 멤버입니다: " + member.getName());
        }
    }
}
