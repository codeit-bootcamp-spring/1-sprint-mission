package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channelDto.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channelDto.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channelDto.FindChannelResponseDto;
import com.sprint.mission.discodeit.dto.channelDto.FindPrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.channelDto.FindPublicChannelResponseDto;
import com.sprint.mission.discodeit.dto.channelDto.UpdatePublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.ChannelDeletedEvent;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;


    // 채널 생성
    // 퍼블릭
    @Override
    public UUID createPublic(CreatePublicChannelRequestDto createPublicChannelRequestDto) {

        UUID ownerId = createPublicChannelRequestDto.ownerId();
        String name = createPublicChannelRequestDto.name();
        String explanation = createPublicChannelRequestDto.explanation();

        Channel channel = new Channel(ownerId, name, explanation);

        channelRepository.save(channel);

        return channel.getId();
    }

    // 프라이빗
    @Override
    public void createPrivate(CreatePrivateChannelRequestDto createPrivateChannelRequestDto) {

        User user = createPrivateChannelRequestDto.user();

        Channel channel = new Channel(user);

        channelRepository.save(channel);
    }

    // 읽기
    // 채널 단건 조회
    @Override
    public FindChannelResponseDto find(UUID id) {

        Channel channel = channelRepository.load().get(id);
        if (channel.isPublic()) {
            return new FindPublicChannelResponseDto(channel);
        } else {
            return new FindPrivateChannelResponseDto(channel);
        }
    }

    // 모든 유저의 모든 채널 반환
    @Override
    public List<FindChannelResponseDto> findAllByUserId(UUID userId) {

        return channelRepository.load().values().stream()
                .map(channel -> {
                    if (channel.isPublic()) {
                        return new FindPublicChannelResponseDto(channel);
                    } else if (channel.getMembers().contains(userId)) {
                        return new FindPrivateChannelResponseDto(channel);
                    } else {
                        return null;
                    }
                })
                .toList();
    }


    @Override
    public void updateChannel(UpdatePublicChannelRequestDto updatePublicChannelRequestDto) {

        if (updatePublicChannelRequestDto.isPublic()) {
            Channel updateChannel = channelRepository.load().get(updatePublicChannelRequestDto.id());

            String updateCategory = updatePublicChannelRequestDto.updateCategory();
            String updateName = updatePublicChannelRequestDto.updateName();
            String updateExplanation = updatePublicChannelRequestDto.updateExplanation();

            updateChannel.updateCategory(updateCategory);
            updateChannel.updateName(updateName);
            updateChannel.updateExplanation(updateExplanation);

            channelRepository.save(updateChannel);
        }
    }

    @Override
    public void addMember(UUID id, UUID memberId) {

        Channel channel = channelRepository.load().get(id);
        channel.addMember(memberId);
        channelRepository.save(channel);
    }

    @Override
    public void deleteMember(UUID id, UUID memberId) {

        Channel channel = channelRepository.load().get(id);

        if (channel.getOwnerId() == memberId) {
            throw new IllegalArgumentException("채널주는 멤버에서 삭제할 수 없습니다.");
        }

        channel.deleteMember(memberId);
        channelRepository.save(channel);
    }

    // 채널 삭제
    @Override
    public void delete(UUID id) {

        channelIsExist(id);

        Channel channel = channelRepository.load().get(id);

        channelRepository.delete(id);

        // 채널 삭제 이벤트 발생
        eventPublisher.publishEvent(new ChannelDeletedEvent(channel));
    }

    // 채널 존재 여부 확인
    @Override
    public void channelIsExist(UUID id) {

        Map<UUID, Channel> channels = channelRepository.load();

        if (!channels.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }

    @Override
    public void updateLastMessageTime(UUID channelID, Instant lastMessageTime) {
        Channel channel = channelRepository.load().get(channelID);
        channel.updateLastMessageTime(lastMessageTime);
    }
}