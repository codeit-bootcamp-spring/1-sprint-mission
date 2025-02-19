package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.channel.PrivateChannelRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.ChannelResponse;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.spirnt.mission.discodeit.enity.*;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    private final ReadStatusService readStatusService;
    private final UserStatusService userStatusService;

    @Override
    public Channel createChannelPublic(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = new Channel(publicChannelCreateRequest.getName(),
            publicChannelCreateRequest.getDescription(), ChannelType.PUBLIC);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel createChannelPrivate(PrivateChannelRequest privateChannelCreateRequest) {
        Channel channel = new Channel(null,null, ChannelType.PRIVATE);
        channelRepository.save(channel);
        // 채널에 참여하는 유저별 ReadStatus 생성
        for(UUID userId: privateChannelCreateRequest.getUsersId()){
            readStatusService.create(new ReadStatusDto(userId, channel.getId(), null));
        }
        return channel;
    }

    @Override
    public ChannelResponse find(UUID userId, UUID channelId) {
        Channel channel= channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        /* Message Response 구성 */
        // PRIVATE 채널인 경우 참여한 유저만 조회 가능
        // ReadStatus 존재 여부로 확인
        if(channel.getType().equals(ChannelType.PRIVATE)&&
                !readStatusService.existsByUserIdChannelId(userId, channelId)) {
            throw new IllegalStateException("User did not joined this private channel");
        }
        // PRIVATE 채널인 경우 참여 유저 찾기
        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            readStatusService.findAllByChannelId(channelId).stream()
                    .map(readStatus->readStatus.getUserId())
                    .forEach(participantIds::add);
        }
        // 해당 채널의 가장 최근 메세지 작성 시간 찾기
        Instant lastMessageAt = findLastMessageInChannel(channelId)
                .orElse(channel.getCreatedAt());    // 채널 내 메세지가 없는 경우 채널 생성 시간을 디폴트로 함

        // ReadStatus 업데이트
        ReadStatus readStatus = readStatusService.findAllByUserId(userId).stream()
                .filter(rs->rs.getChannelId().equals(channelId) && rs.getUserId().equals(userId))
                .findAny()
                .orElse(null);
        if(readStatus!=null) readStatusService.update(readStatus.getId(), new ReadStatusDto(userId, channelId,Instant.now()));
        // UserStatus 업데이트 -> 온라인 && 현재 활동 중으로 간주
        userStatusService.updateByUserId(userId, new UserStatusUpdate(UserStatusType.ONLINE, Instant.now()));

        return new ChannelResponse(channel, participantIds, lastMessageAt);
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        // UserStatus 업데이트 -> 온라인 && 현재 활동 중으로 간주
        userStatusService.updateByUserId(userId, new UserStatusUpdate(UserStatusType.ONLINE, Instant.now()));
        Map<UUID, Channel> data = channelRepository.findAll();
        return data.values().stream()
                //PUBLIC이거나 User가 참여한 PRIVATE 채널이거나
                //참여 여부는 ReadStatus 존재 여부로 확인
                .filter(channel -> (channel.getType() == ChannelType.PUBLIC) ||
                        (channel.getType() == ChannelType.PRIVATE && readStatusService.existsByUserIdChannelId(userId, channel.getId())))
                .sorted(Comparator.comparing(channel -> channel.getCreatedAt()))
                // ChannelResponse로 mapping
                .map(channel -> find(userId, channel.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Channel update(UUID channelId, ChannelUpdateRequest channelUpdateRequest) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        // PRIVATE 채널은 업데이트할 수 없음
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("Private Channel Cannot be updated");
        }
        channel.update(channelUpdateRequest.getName(), channelUpdateRequest.getDescription());
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if(!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel ID: " + channelId + " Not Found");
        }
        // 채널에 속한 메세지 삭제
        Map<UUID, Message> data = messageRepository.findAll();
        if (data == null) return;
        data.values().stream()
                .forEach(message -> {
                    // 해당 채널에 쓰여진 메세지 객체를 찾고
                    if (message.getChannelId().equals(channelId)) {
                        // 그 객체를 레포지토리에서 삭제
                        messageRepository.delete(message.getId());
                    }
                });
        // 채널과 관련된 ReadStatus 삭제
        readStatusService.deleteByChannelId(channelId);
        // 채널 삭제
        channelRepository.delete(channelId);
    }


    // 해당 채널 메세지를 정렬하여 가장 최근 메세지 시간 찾기
    public Optional<Instant> findLastMessageInChannel(UUID channelId) {
        List<Message> messages = messageRepository.findAll().values().stream().toList();
        Optional<Instant> lastSeenAt = messages.stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .findFirst();
        return lastSeenAt;
    }
}


