package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public Channel createPublicChannel(PublicChannelCreateRequest request) {
        // 1. 파라미터 예외처리, 변수에 넣기 2. 객체 만들기 3. repository.save
        String channelName = request.name();
        String channelTopic = request.topic();
        Channel channel = new Channel(channelName, channelTopic, ChannelType.PUBLIC);
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        Channel createdChannel = channelRepository.save(channel);
        for (UUID userId : request.participantIds()) {
            ReadStatus readStatus = new ReadStatus(userId, createdChannel.getId(), Instant.MIN); // Instant.Min : 초기값
            readStatusRepository.save(readStatus);
        }
        return createdChannel;
    }

    @Override
    // 가장 최근 메세지 정보를 담을 거니까 channelDto 반환.
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("해당 채널이 존재하지 않습니다."));
    }


    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository.findAll();
        List<ChannelDto> returnChannels = new ArrayList<>();
        for(Channel channel : publicChannels) {
            if(channel.getType().equals(ChannelType.PUBLIC)) {
                returnChannels.add(toDto(channel));
            }else if(channel.getType().equals(ChannelType.PRIVATE)){
                Optional<ReadStatus> optionalReadStatus = readStatusRepository.findById(channel.getId());
                if (optionalReadStatus.isPresent() && optionalReadStatus.get().getUserId().equals(userId)) {
                    returnChannels.add(toDto(channel));
                }
            }
        }
        return returnChannels;
    }


    @Override
    public Channel update(UUID channelId, ChannelUpdateRequest request) {
        // private 채널이면 수정할 수 없다는 말
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(()-> new NoSuchElementException("해당 채널이 존재하지 않습니다."));
        if (channel.getType().equals(ChannelType.PRIVATE)) {
                System.out.println("PRIVATE 채널은 수정할 수 없습니다.");
        }else{
            String newName = request.name();
            String newTopic = request.topic();
            channel.update(newName, newTopic);
            }
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        // 삭제 전에 채널 정보 가져오기 (로그를 위해)
        Optional<Channel> channelToDelete = channelRepository.findById(channelId);
        if (channelToDelete.isEmpty()) {
            System.out.println("삭제하려는 채널이 존재하지 않습니다: ID = " + channelId);
        }
        // 관련 도메인 데이터 삭제
        readStatusRepository.deleteAllByChannelId(channelId);
        messageRepository.deleteAllByChannelId(channelId);

        // 채널 삭제
        channelRepository.deleteById(channelId);
    }

    private ChannelDto toDto(Channel channel){
        // 1. lastMessageAt과 participantIds 포함한 채널dto 값 반환
        // 2. lastMessageAt과 participantIds 정의
        // 3. private만 participantIds 포함하도록

        Instant lastMessageAt = Instant.MIN; // 초기값은 가장 과거의 시간인 .MIN으로 초기화
        List<Message> msgs = messageRepository.findAllByChannelId(channel.getId());
        for (Message msg : msgs){
            if(msg.getCreatedAt().isAfter(lastMessageAt)){
                lastMessageAt = msg.getCreatedAt();
            }
        }

        List<UUID> participantIds = new ArrayList<>();
        if(channel.getType().equals(ChannelType.PRIVATE)){
            readStatusRepository.findAllByChannelId(channel.getId())
                    .forEach(readStatus -> participantIds.add(readStatus.getUserId()));
                    // .forEach()는 Iterable에서 제공하는 메서드라서 .stream() 안써도 된다.

        }

        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getTopic(),
                participantIds,
                lastMessageAt
        );
    }
}
