package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.ChannelRepository;
import com.sprint.mission.discodeit.repository.interfacepac.MessageRepository;
import com.sprint.mission.discodeit.repository.interfacepac.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;


    @Override
    public ChannelResponseDTO createPrivateChannel(PrivateChannelCreateDTO requestDTO) {
        //채널 소유자 확인
        User owner = userRepository.findById(requestDTO.ownerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        // 참여멤버 조회?? 너무 데이터베이스 성능이 저하될꺼 같음..
        List<User> members = requestDTO.memberIds().stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("User not found")))
                .toList();
        //멤버가 한명이상이어야 됨.
        if(members.isEmpty()) {
            throw new IllegalArgumentException("At least one member must be added to the private channel.");
        }
        //Private 채널 생성
        Channel privateChannel = new Channel(owner, ChannelType.PRIVATE);
        channelRepository.save(privateChannel);

        for (User member : members) {
            ReadStatus readStatus = new ReadStatus(member, privateChannel, Instant.now());
            readStatusRepository.save(readStatus); // 레포지토리 구현 해야함 확인!
        }
        List<UUID>memberIds = members.stream()
                .map(User::getId)
                .toList();
        //응답 반환
        return new ChannelResponseDTO(privateChannel.getId(),null,null, ChannelType.PRIVATE, memberIds,null);
    }

    @Override
    public ChannelResponseDTO createPublicChannel(PublicChannelCreateDTO requestDTO) {
        //채널 소유자 확인
        User owner = userRepository.findById(requestDTO.ownerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        //Public 채널 생성
        Channel publicChannel = new Channel(owner, requestDTO.name(), requestDTO.description(), ChannelType.PUBLIC);
        channelRepository.save(publicChannel);
        //응답 반환
        return new ChannelResponseDTO(publicChannel.getId(),publicChannel.getName(),publicChannel.getDescription(), ChannelType.PUBLIC,null,null);

    }

    @Override
    public ChannelResponseDTO find(UUID channelId) {
        //채널 조회
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));
        //Private 채널이면 사용자 id 목록 포함
        List<UUID>memberIds = null;
        if(channel.getType() == ChannelType.PRIVATE) {
            memberIds = readStatusRepository.findAllByChannel(channel) // 이부분 개선가능 성능 향상
                    .stream()
                    .map(readStatus -> readStatus.getUser().getId())
                    .toList();
        }
        // 응답 DTO 생성
        return new ChannelResponseDTO(
                channel.getId(),
                channel.getType() == ChannelType.PUBLIC ? channel.getName() : null,
                channel.getType() == ChannelType.PUBLIC ? channel.getDescription() : null,
                channel.getType(),
                memberIds,
                channel.getLastMessageAt() != null ? channel.getLastMessageAt() : Instant.EPOCH //null 방지..?
        );

    }

    @Override
    public List<ChannelResponseDTO> findAllByUserId(UUID userId) {
        //사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        //private 채널 조회(사용자가 참여한 채널 + 사용자가 소유한 채널) -> 레포지토리 내부에서 읽고 확인하는 로직 필요
        List<Channel> privateChannels = readStatusRepository.findChannelsByUser(user);
        channelRepository.findAllByOwnerAndType(user, ChannelType.PRIVATE);

        //중복제거
        HashSet<Channel> uniquePrivateChannels = new HashSet<>(privateChannels);
        uniquePrivateChannels.addAll(privateChannels);

        //public 채널 조회 (모든 사용자가 접근 가능)
        List<Channel>publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);

        //private 채널 응답 반환
        List<ChannelResponseDTO> privateChannelResponses = uniquePrivateChannels.stream()
                .map(channel -> new ChannelResponseDTO(
                        channel.getId(),
                        null,
                        null,
                        ChannelType.PRIVATE,
                        readStatusRepository.findUserIdsByChannel(channel),
                        channel.getLastMessageAt() != null ? channel.getLastMessageAt() : Instant.EPOCH
                ))
                .toList();

        //public 채널 응답 반환
        List<ChannelResponseDTO> publicChannelResponses = publicChannels.stream()
                .map(channel -> new ChannelResponseDTO(
                        channel.getId(),
                        channel.getName(),
                        channel.getDescription(),
                        ChannelType.PUBLIC,
                        null,
                        channel.getLastMessageAt() != null ? channel.getLastMessageAt() : Instant.EPOCH
                ))
                .toList();
        // public 채널 + private 채널 -> 전체 목록
        List<ChannelResponseDTO>allChannels = new ArrayList<>();
        allChannels.addAll(privateChannelResponses);
        allChannels.addAll(publicChannelResponses);

        return allChannels;
    }

    @Override
    public ChannelResponseDTO update(ChannelUpdateDTO updateDTO) {
        //채널 조회
        Channel channel = channelRepository.findById(updateDTO.channelId())
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        if(channel.getType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("Private channels are not supported");
        }

        channel.update(updateDTO.name(), updateDTO.description());

        channelRepository.save(channel);

        //변경 정보 DTO로 변환 후 반환
        return new ChannelResponseDTO(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                null,
                channel.getLastMessageAt() != null ? channel.getLastMessageAt() : Instant.EPOCH
        );
    }

    @Override
    public void delete(UUID channelId) {
        //채널 조회
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        //관련 데이터 삭제 (메세지, 사용자 상태)
        messageRepository.deleteByChannel(channel);
        readStatusRepository.deleteByChannel(channel);
        //채널 삭제
        channelRepository.deleteByChannel(channel);

        System.out.println("Channel deleted: " + channelId);
    }
}
