package com.sprint.mission.discodeit.service.file;

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

@Service("fileChannelService")
@RequiredArgsConstructor
public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    /** ✅ 공개 채널 생성 */
    @Override
    public ChannelResponse createPublicChannel(ChannelCreateRequest channelCreateRequest) {
        return createChannel(channelCreateRequest, false); // 공개 채널 (isPrivate=false)
    }

    /** ✅ 비공개 채널 생성 */
    @Override
    public ChannelResponse createPrivateChannel(ChannelCreateRequest channelCreateRequest) {
        return createChannel(channelCreateRequest, true); // 비공개 채널 (isPrivate=true)
    }

    /** ✅ 공통 채널 생성 메서드 */
    private ChannelResponse createChannel(ChannelCreateRequest channelCreateRequest, boolean isPrivate) {
        Channel channel = new Channel(
                channelCreateRequest.getName(),
                channelCreateRequest.getDescription(),
                channelCreateRequest.getCreatorId(),
                isPrivate,
                channelCreateRequest.getMembers()
        );
        channelRepository.save(channel);
        return convertToDTO(channel);
    }

    /** ✅ 모든 채널 조회 */
    @Override
    public List<ChannelResponse> readAll() {
        return channelRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    /** ✅ 특정 채널 조회 */
    @Override
    public Optional<ChannelResponse> read(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::convertToDTO);
    }

    /** ✅ 채널 업데이트 */
    @Override
    public void update(UUID channelId, ChannelUpdateRequest channelUpdateRequest) {
        Optional<Channel> optionalChannel = channelRepository.findById(channelId);
        optionalChannel.ifPresentOrElse(channel -> {
            if (channel.isPrivate()) {
                throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
            }
            channel.setName(channelUpdateRequest.getName());
            channel.setDescription(channelUpdateRequest.getDescription());
            channelRepository.save(channel);
        }, () -> {
            throw new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다.");
        });
    }

    /** ✅ 채널 삭제 */
    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.findById(channelId).isPresent()) {
            throw new NoSuchElementException("존재하지 않는 채널입니다.");
        }
        channelRepository.deleteById(channelId);
    }

    /** ✅ 특정 사용자가 접근 가능한 채널 조회 */
    @Override
    public List<ChannelResponse> getChannelsForUser(UUID userId) {
        return channelRepository.findAllChannelsForUser(userId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    /** ✅ 엔티티를 DTO로 변환 */
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
