package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.domain.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private static volatile BasicChannelService instance;

    public static BasicChannelService getInstance(ChannelRepository channelRepository, MessageRepository messageRepository, ReadStatusRepository readStatusRepository) {
        if (instance == null) {
            synchronized (BasicChannelService.class) {
                if (instance == null) {
                    instance = new BasicChannelService(channelRepository, messageRepository, readStatusRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public ChannelDto createPrivateChannel(ChannelDto dto, List<UUID> userList) {
        //private 신청 검토
        Channel savedChannel = channelRepository.save(dto.type());
        userList.forEach(userId -> readStatusRepository.save(new ReadStatus(userId, savedChannel.getId())));
        return new ChannelDto(savedChannel);
    }

    @Override
    public ChannelDto createPublicChannel(ChannelDto dto) {
        //public 신청 검토
        Channel savedChannel = channelRepository.save(dto.name(), dto.type());
        return  new ChannelDto(savedChannel);
    }

    @Override
    public Channel getChannel(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> getChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID id, String name) {
        channelRepository.update(id, name);
    }

    @Override
    public void deleteChannel(UUID id) {
        channelRepository.delete(id);
    }
}
