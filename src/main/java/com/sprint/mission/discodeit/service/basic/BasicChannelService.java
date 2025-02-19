package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public Channel create(ChannelType type, String channelName, User admin) {
        if (userRepository.existsId(admin.getId())) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }
        Channel channel = new Channel(type, channelName, admin);
        return channelRepository.save(channel);
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, UUID adminId, String newChannelName) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

        if(!channel.getAdmin().getId().equals(adminId)){
            throw new IllegalArgumentException("관리자만 수정할 수 있습니다.");
        }

        channel.update(newChannelName);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId, UUID adminId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

        if (!channel.getAdmin().getId().equals(adminId)) {
            throw new IllegalArgumentException("관리자만 삭제할 수 있습니다.");
        }
    }

    @Override
    public void joinChannel(UUID channelId, User user) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
        channel.addMember(user);
        channelRepository.save(channel);
    }

    @Override
    public void leaveChannel(UUID channelId, User user) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
        channel.deleteMember(user);
        channelRepository.save(channel);
    }
}
