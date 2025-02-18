package com.sprint.mission.discodeit.service.impl;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ReadStatusService readStatusService;

    @Override
    public Channel createPrivateChannel(ChannelDTO.PrivateChannelDTO privateChannelDTO) {
        Channel channel = new Channel();
        channel.setType(ChannelType.PRIVATE);
        // private 채널은 name, description 없이 생성

        // private 채널에 참여할 유저들의 정보 처리
        privateChannelDTO.getUserIds().forEach(userId -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            // User 별 ReadStatus 생성
            readStatusService.create(user, channel);
        });

        return channelRepository.save(channel);
    }

    @Override
    public Channel createPublicChannel(ChannelDTO.PublicChannelDTO publicChannelDTO) {
        Channel channel = new Channel();
        channel.setType(ChannelType.PUBLIC);
        channel.setName(publicChannelDTO.getName());
        channel.setDescription(publicChannelDTO.getDescription());

        return channelRepository.save(channel);
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new RuntimeException("Channel not found"));
        // 해당 채널의 가장 최근 메시지 시간 포함
        // 참여한 User의 ID 포함 (PRIVATE 채널인 경우)
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        // USER가 볼 수 있는 채널 목록 조회
        List<Channel> channels = channelRepository.findAll();  // 예시로 전체 채널을 조회

        // PRIVATE 채널의 경우 해당 유저가 참여한 채널만 필터링
        return channels;
    }

    @Override
    public Channel update(UUID channelId, ChannelDTO.UpdateChannelDTO updateChannelDTO) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new RuntimeException("Channel not found"));

        // PRIVATE 채널은 수정할 수 없으므로 예외 처리
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new RuntimeException("Private channels cannot be updated");
        }

        channel.setName(updateChannelDTO.getNewName());
        channel.setDescription(updateChannelDTO.getNewDescription());

        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new RuntimeException("Channel not found"));

        // 관련된 Message, ReadStatus 삭제
        messageService.deleteByChannelId(channelId);
        readStatusService.deleteByChannelId(channelId);

        // 채널 삭제
        channelRepository.delete(channel);
    }
}
