package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.FindPrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.FindPublicChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.UpdatePublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.ChannelDeletedEvent;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.ChannelType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

        User owner = createPublicChannelRequestDto.user();
        userService.userIsExist(owner.getId());

        String name = createPublicChannelRequestDto.name();
        String explanation = createPublicChannelRequestDto.explanation();

        Channel channel = new Channel(owner, name, explanation);

        channelRepository.save(channel);

        return channel.getId();
    }

    // 프라이빗
    @Override
    public UUID createPrivate(CreatePrivateChannelRequestDto createPrivateChannelRequestDto) {

        UUID ownerId = createPrivateChannelRequestDto.ownerId();

        User owner = userService.find(ownerId);

        Channel channel = new Channel(owner);

        channelRepository.save(channel);

        return channel.getId();
    }

    // 읽기
    // 채널 단건 조회
    @Override
    public Channel find(UUID id) {

        channelIsExist(id);

        return channelRepository.load().get(id);
    }

    // 모든 유저의 모든 채널 반환
    @Override
    public List<FindChannelResponseDto> findAllByUserId(UUID userId) {

        userService.userIsExist(userId);

        return channelRepository.load().values().stream()
                .map(channel -> {
                    if (channel.getType().equals(ChannelType.PUBLIC)) {
                        return FindPublicChannelResponseDto.fromEntity(channel);
                    } else if (channel.getMembers().contains(userId)) {
                        return FindPrivateChannelResponseDto.fromEntity(channel);
                    } else {
                        return null;
                    }
                })
                .toList();
    }

    // 공개 채널 수정
    @Override
    public FindChannelResponseDto updateChannel(UpdatePublicChannelRequestDto updatePublicChannelRequestDto) {

        channelIsExist(updatePublicChannelRequestDto.id());

        Channel updateChannel = channelRepository.load().get(updatePublicChannelRequestDto.id());

        if (updateChannel.getType().equals(ChannelType.PUBLIC)) {
            String updateCategory = updatePublicChannelRequestDto.category();
            String updateName = updatePublicChannelRequestDto.name();
            String updateExplanation = updatePublicChannelRequestDto.explanation();

            updateChannel.updateName(updateName);
            updateChannel.updateCategory(updateCategory);
            updateChannel.updateDescription(updateExplanation);

            channelRepository.save(updateChannel);

            if (updateChannel.getType().equals(ChannelType.PUBLIC)) {
                return FindPublicChannelResponseDto.fromEntity(updateChannel);
            } else {
                return FindPrivateChannelResponseDto.fromEntity(updateChannel);
            }
        }

        throw new NoSuchElementException("채널이 존재하지 않습니다.");
    }

    // 멤버 추가
    @Override
    public void addMember(UUID id, UUID userId) {

        channelIsExist(id);

        Channel channel = channelRepository.load().get(id);
        User user = userService.find(channel.getOwner().getId());
        channel.addMember(user);
        channelRepository.save(channel);
    }

    // 멤버 삭제
    @Override
    public void deleteMember(UUID id, UUID memberId) {

        channelIsExist(id);

        Channel channel = channelRepository.load().get(id);
        User user = userService.find(memberId);

        if (channel.getOwner().equals(user)) {
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

        // 채널 삭제 이벤트 발생
        eventPublisher.publishEvent(new ChannelDeletedEvent(channel));

        channelRepository.delete(id);

    }

    // 채널 존재 여부 확인
    @Override
    public void channelIsExist(UUID id) {

        Map<UUID, Channel> channels = channelRepository.load();

        if (!channels.containsKey(id)) {
            throw new NoSuchElementException("존재하지 않는 채널입니다.");
        }
    }

    @Override
    public void updateLastMessageTime(UUID channelID, Instant lastMessageTime) {
        Channel channel = channelRepository.load().get(channelID);
        channel.updateLastMessageTime(lastMessageTime);
    }
}