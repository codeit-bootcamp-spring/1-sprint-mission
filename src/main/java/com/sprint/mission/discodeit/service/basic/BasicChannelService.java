package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelFindAllDto;
import com.sprint.mission.discodeit.dto.ChannelFindDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.domain.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    @Override
    public ChannelDto create(ChannelDto dto, List<UUID> userList) {
        //private 신청 검토
        Channel savedChannel = channelRepository.save(dto.type());
        userList.forEach(userId -> readStatusRepository.save(new ReadStatus(userId, savedChannel.getId())));
        return new ChannelDto(savedChannel);
    }

    @Override
    public ChannelDto create(ChannelDto dto) {
        //public 신청 검토
        Channel savedChannel = channelRepository.save(dto.name(), dto.type());
        return new ChannelDto(savedChannel);
    }

    @Override
    public ChannelFindDto find(UUID id) {
        Channel channel = channelRepository.findById(id);
        List<Message> messagesById = messageRepository.findMessagesById(id);
        Instant updatedAt = messagesById.stream().min(Comparator.comparing(Message::getUpdatedAt)).get().getUpdatedAt();//가장 최근의 메시지 시간 정보

        if (channel.getType() == ChannelType.PRIVATE) {
            //참여한 User의 ID 정보 포함
            List<ReadStatus> privateChannelUsers = readStatusRepository.findAllByChannelId(id);
            List<UUID> userList = privateChannelUsers.stream().map(ReadStatus::getUserId).toList();
            return new ChannelFindDto(channel, updatedAt, userList);
        } else {
            return new ChannelFindDto(channel, updatedAt);
        }
    }

    @Override
    public ChannelFindAllDto findAll() {
        List<Channel> channels = channelRepository.findAll();
        Map<UUID, Instant> latestMessagesInstant = new HashMap<>();
        List<UUID> list = channels.stream().map(Channel::getId).toList(); //채널 ID
        for (UUID uuid : list) {
            List<Message> messagesById = messageRepository.findMessagesById(uuid);
            Instant updatedAt = messagesById.stream().min(Comparator.comparing(Message::getUpdatedAt)).get().getUpdatedAt();
            latestMessagesInstant.put(uuid, updatedAt);
        }
        List<List<UUID>> userList = new ArrayList<>();
        for (Channel channel : channels) {
            if (channel.getType() == ChannelType.PRIVATE) {
                //참여한 User의 ID 정보 포함
                List<ReadStatus> privateChannelUsers = readStatusRepository.findAllByChannelId(channel.getId());
                userList.add(privateChannelUsers.stream().map(ReadStatus::getUserId).toList());
            }
        }
        return new ChannelFindAllDto(channels, latestMessagesInstant, userList);
        //채널, 채널마다 시간 정보, 유저 리스트
    }

    @Override
    public ChannelFindAllDto findAllByUserId(UUID userId) {
        List<Channel> channels = readStatusRepository.findAllByUserId(userId).stream().map(ReadStatus::getChannelId).map(s -> channelRepository.findById(s)).toList();
        Map<UUID, Instant> latestMessagesInstant = new HashMap<>();
        List<UUID> list = channels.stream().map(Channel::getId).toList(); //채널 ID
        for (UUID uuid : list) {
            List<Message> messagesById = messageRepository.findMessagesById(uuid);
            Instant updatedAt = messagesById.stream().min(Comparator.comparing(Message::getUpdatedAt)).get().getUpdatedAt();
            latestMessagesInstant.put(uuid, updatedAt);
        }
        return new ChannelFindAllDto(channels, latestMessagesInstant, null);
    }

    @Override
    public void updateChannel(ChannelDto dto) {
        channelRepository.update(dto.id(), dto.name());
    }

    @Override
    public void deleteChannel(UUID id) {
        //채널과 관련된 메시지 삭제 (Message.ser 속 객체 )
        List<UUID> messages = channelRepository.messages(id);
        for (UUID message : messages) {
            messageRepository.delete(message);
        }
        //채널 삭제 ( 내부 Map )
        channelRepository.delete(id);
        //채널과 관련된 ReadStatus 삭제
        readStatusRepository.deleteByChannelId(id);
    }
}
