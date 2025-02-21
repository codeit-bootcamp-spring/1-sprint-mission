package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.ResponseChannelDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service //서비스 Bean
@RequiredArgsConstructor // 생성자
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    @Override
    public Channel savePublic(PublicChannelDto publicChannelDto) {
        if(publicChannelDto.channelType() != Channel.ChannelType.PUBLIC) {
            throw new NoSuchElementException("The channel type must be PUBLIC");
        }
        Channel channel = new Channel(publicChannelDto.channelType(),publicChannelDto.channelName(),publicChannelDto.description());
        return channelRepository.save(channel);
    }
    @Override
    public Channel savePrivate(PrivateChannelDto privateChannelDto) {
        if(privateChannelDto.channelType() != Channel.ChannelType.PRIVATE) {
            throw new NoSuchElementException("The channel type must be PRIVATE");
        }
        List<User> users = userRepository.findAllById(privateChannelDto.userIds());
        if(users.isEmpty()) {
            throw new NoSuchElementException("No users found");
        }
        Channel channel = new Channel(Channel.ChannelType.PRIVATE);
        channelRepository.save(channel);

        List<ReadStatus> readStatuses = users.stream()
                .map(user -> new ReadStatus(user,channel,Instant.now(),true))
                .toList();
        readStatusRepository.saveAll(readStatuses);

        return channel;
    }


    @Override
    public ResponseChannelDto findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("The channel with id " + channelId + " does not exist"));
        //해당 채널의 가장 최근 메시지 시간 조회
        Instant latestMessageTime = getLastMessageTime(channelId);

        // PRIVATE 채널인 경우 참여한 User ID 조회
        List<UUID> participantUserIds = new ArrayList<>();
        if (channel.getChannelType() == Channel.ChannelType.PRIVATE) {
            participantUserIds = readStatusRepository.findAll().stream()
                    .filter(rs -> rs.getChannel().getId().equals(channelId))
                    .map(rs->rs.getUser().getId())
                    .toList();
        }

        return ResponseChannelDto.from(channel, latestMessageTime, participantUserIds);

    }

    @Override
    public List<ResponseChannelDto> findAllByUserId(UUID userId) {
        List<ResponseChannelDto> reponseList = new ArrayList<>();

        //PUBLIC
        List<Channel> publicChannels = channelRepository.findAll().stream()
                .filter(c->c.getChannelType() == Channel.ChannelType.PUBLIC)
                .toList();

        for(Channel c : publicChannels){
            Instant latestMessageTime = getLastMessageTime(c.getId());
            ResponseChannelDto dto = new ResponseChannelDto(c.getId(),c.getChannelType(),c.getChannelName(),latestMessageTime,null);
            reponseList.add(dto);
        }

        //PRIVATE
        List<UUID> privateChannelIds = readStatusRepository.findAll().stream()
                .filter(rs->rs.getUser().getId().equals(userId))
                .map(rs->rs.getChannel().getId())
                .toList();
        List<Channel> privateChannels = channelRepository.findAll().stream()
                .filter(c ->c.getChannelType()== Channel.ChannelType.PRIVATE && privateChannelIds.contains(c.getId()))
                .toList();

        for(Channel c : privateChannels){
            Instant latestMessageTime = getLastMessageTime(c.getId());
            List<UUID> participanUserIds = readStatusRepository.findAll().stream()
                    .filter(rs->rs.getChannel().getId().equals(c.getId()))
                    .map(rs->rs.getUser().getId())
                    .toList();
            ResponseChannelDto dto = ResponseChannelDto.from(c, latestMessageTime, participanUserIds);
            reponseList.add(dto);
        }
        return  reponseList;

    }
    //가장 최근 메시지 시간을 구하는 메서드
    public Instant getLastMessageTime(UUID channelId) {
        Instant latestMessageTime = messageRepository.findAll().stream()
                .filter(m -> m.getChannel().getId().equals(channelId))
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
        return latestMessageTime;
    }

    @Override
    public Channel update(ChannelUpdateDto channelUpdateDto) {
        Channel channel = channelRepository.findById(channelUpdateDto.channelId()).orElseThrow(()->new NoSuchElementException("Channel not found"));
        if(channel.getChannelType() == Channel.ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("Private channels are not supported");
        }
        // PUBLIC 채널만 수정 가능
        if(channel.getChannelType() == Channel.ChannelType.PUBLIC) {
            channel.updateChannel(
                    channelUpdateDto.channelName(),
                    channelUpdateDto.description()
            );
        }
        return channelRepository.save(channel);
    }

    @Override
    public void deleteById(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        //관련된 ReadStatus 삭제
        readStatusRepository.deleteById(channelId);
        //관련된 Message 삭제
        messageRepository.deleteById(channelId);
        //채널 삭제
        channelRepository.deleteById(channelId);

    }
}
