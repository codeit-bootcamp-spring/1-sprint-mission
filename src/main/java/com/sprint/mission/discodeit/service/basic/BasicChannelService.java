package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelMemberRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelMapper channelMapper;

    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ChannelResponseDto createPublicChannel(PublicChannelRequestDto channelReqDTO) {
        Channel savedChannel = channelRepository.saveAndFlush(channelMapper.toPublicEntity(channelReqDTO));
        return channelMapper.toResponseDto(savedChannel);
    }

    @Transactional
    @Override
    public ChannelResponseDto createPrivateChannel(PrivateChannelRequestDto channelReqDTO) {
        Channel channel = channelMapper.toPrivateEntity(channelReqDTO);
        Channel savedChannel = channelRepository.save(channel);

        List<User> users = channelReqDTO.participantIds().stream()
                .map(userRepository::findById)
                .flatMap(Optional::stream)
                .toList();

        // 3. ChannelMember 엔티티 생성 및 저장
        List<ChannelMember> channelMembers = users.stream()
                .map(user -> new ChannelMember(user, savedChannel))
                .toList();

        channelMemberRepository.saveAll(channelMembers);  // ✅ 명시적으로 저장 (더 안전함)
        return channelMapper.toResponseDto(savedChannel);
    }

    @Override
    public ChannelResponseDto find(UUID uuid) {
        return channelMapper.toResponseDto(channelRepository.findById(uuid).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA)));
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        return channelRepository.findAllByUserId(userId).stream()
                .map(channelMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ChannelResponseDto update(UUID channelId, ChannelUpdateDto updateDTO) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
        if (channel.getChannelType() == Channel.ChannelType.PRIVATE) {
            throw new CustomException(ErrorCode.PRIVATE_CANNOT_MODIFY);
        }

        channel.updateServerName(updateDTO.name());
        channel.updateDescription(updateDTO.description());

        return channelMapper.toResponseDto(channel);
    }

    @Transactional
    @Override
    public ChannelResponseDto delete(UUID channelId) {
        Channel deleteChannel = channelRepository.findById(channelId).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
        channelRepository.delete(deleteChannel);
        return channelMapper.toResponseDto(deleteChannel);
    }
}
