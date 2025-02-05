package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelValidator validator;

    @Override
    public Channel create(String name, String introduction, User owner) {
        validator.validate(name, introduction);
        return channelRepository.save(name, introduction, owner);
    }

    @Override
    public Channel find(UUID channelId) {
        return Optional.ofNullable(channelRepository.find(channelId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public String getInfo(UUID channelId) {
        return find(channelId).toString();
    }

    @Override
    public void update(UUID channelId, String name, String introduction) {
        validator.validate(name, introduction);
        Channel channel = find(channelId);
        channelRepository.update(channel, name, introduction);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = find(channelId);
        channelRepository.delete(channel);
    }
}
