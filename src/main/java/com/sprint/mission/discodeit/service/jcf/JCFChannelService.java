package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service("jcfChannelService")
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    // ✅ 공개 채널 생성
    @Override
    public ChannelResponse createPublicChannel(ChannelCreateRequest channelCreateRequest) {
        return createChannel(channelCreateRequest, false);
    }

    // ✅ 비공개 채널 생성
    @Override
    public ChannelResponse createPrivateChannel(ChannelCreateRequest channelCreateRequest) {
        return createChannel(channelCreateRequest, true);
    }

    // ✅ 채널 생성 공통 로직 (공개/비공개)
    private ChannelResponse createChannel(ChannelCreateRequest channelCreateRequest, boolean isPrivate) {
        Channel channel = new Channel(
                UUID.randomUUID(),
                channelCreateRequest.getName(),
                channelCreateRequest.getDescription(),
                channelCreateRequest.getCreatorId(),
                isPrivate,
                Instant.now(),
                channelCreateRequest.getMembers()
        );
        channelRepository.save(channel);
        return convertToDTO(channel);
    }

    // ✅ 모든 채널 조회
    @Override
    public List<ChannelResponse> readAll() {
        return channelRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ✅ 특정 채널 조회
    @Override
    public Optional<ChannelResponse> read(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::convertToDTO);
    }

    // ✅ 특정 사용자가 참여할 수 있는 채널 조회 (이 메서드가 없어서 오류 발생)
    @Override
    public List<ChannelResponse> getChannelsForUser(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel -> channel.isPublic() || channel.getMembers().contains(userId))
                .map(this::convertToDTO)
                .toList();
    }

    // ✅ 채널 업데이트 (PATCH 방식 유지, 비공개 채널 수정 불가)
    @Override
    public void update(UUID channelId, ChannelUpdateRequest channelUpdateRequest) {
        channelRepository.findById(channelId).ifPresentOrElse(channel -> {
            if (channel.isPrivate()) {
                throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
            }
            channel.setName(channelUpdateRequest.getName());
            channel.setDescription(channelUpdateRequest.getDescription());
            channelRepository.save(channel);
        }, () -> {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        });
    }

    // ✅ 채널 삭제 (204 응답 보장, 존재 여부 체크)
    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        channelRepository.deleteById(channelId);
    }

    // ✅ `Channel` → `ChannelResponse` 변환 메서드
    private ChannelResponse convertToDTO(Channel channel) {
        return new ChannelResponse(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getCreatorId(),
                channel.isPrivate(),
                channel.getCreatedAt(),
                channel.getMembers()
        );
    }
}
