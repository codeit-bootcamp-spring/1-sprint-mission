package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    public BasicChannelService(ChannelRepository channelRepository){this.channelRepository = channelRepository;}

    @Override
    public boolean createChannel(Channel channel) {
        boolean created = channelRepository.save(channel);
        if(created){
            System.out.println("생성된 채널: " + channel);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public Optional<Channel> readChannel(UUID id) {
        Optional<Channel> ch = channelRepository.findById(id);
        ch.ifPresent(c -> System.out.println("조회된 채널: " + c));
        return ch;
    }

    @Override
    public List<Channel> readAllChannels() {
        List<Channel> channels = channelRepository.findAll();
        if(channels != null && !channels.isEmpty()){
            System.out.println("전체 채널 목록: " + channels);
            return channels;
        } else {
            System.out.println("채널 목록이 비어 있습니다.");
            return Collections.emptyList(); // 비어 있을 경우 빈 리스트 반환
        }
    }

    @Override
    public void updateChannel(UUID id, String name, String topic) {
        boolean updated = channelRepository.updateOne(id, name, topic);
        if (updated) {
            Optional<Channel> ch = channelRepository.findById(id);
            ch.ifPresent(c -> System.out.println("수정된 채널: " + c));
            List<Channel> allChannels = channelRepository.findAll();
            System.out.println("수정 후 전체 채널 목록: " + allChannels);
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        boolean deleted = channelRepository.deleteOne(id);
        if (deleted){
            Optional<Channel> ch = channelRepository.findById(id);
            System.out.println("삭제된 채널: " + ch); // 존재안하면 안하는데로 Optional.isEmpty 가 뜨는지 확인
            List<Channel> allChannels = channelRepository.findAll();
            System.out.println("삭제 후 전체 채널 목록: " + allChannels);
        }
    }
}
