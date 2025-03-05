package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.EntryUtils;
import com.sprint.mission.discodeit.util.validation.ChannelValidator;
import com.sprint.mission.discodeit.util.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final Validator<Channel, ChannelDTO.request> channelValidator = new ChannelValidator();
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public CommonDTO.idResponse createPublicChannel(ChannelDTO.request channelReqDTO) {
        // 생성
        try {
            channelValidator.validateCreate(channelReqDTO);
            Channel channel = new Channel(
                    channelReqDTO.owner(), channelReqDTO.serverName(), channelReqDTO.channelType(), channelReqDTO.description()
            );
            Long channelId = channelRepository.save(channel);

            return CommonDTO.idResponse.from(channelId, channel.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommonDTO.idResponse createPrivateChannel(ChannelDTO.request channelReqDTO) {
        try {
            channelValidator.validateCreate(channelReqDTO);
            Long channelId = channelRepository.save(
                    new Channel(channelReqDTO.owner(), channelReqDTO.serverName(), channelReqDTO.channelType(), channelReqDTO.description())
            );
            Channel channel = channelRepository.load(channelId);

            if (channelReqDTO.members() != null && !channelReqDTO.members().isEmpty()) {
                for (UUID userId : channelReqDTO.members()) {
                    channel.addMember(userId);  // 멤버 추가
                    readStatusRepository.save(new ReadStatus(userId, channel.getId(), channelReqDTO.recent()));
                }
            }
            return CommonDTO.idResponse.from(channelId, channel.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChannelDTO.response find(Long id) {
        Channel channel = channelRepository.load(id);
        Instant readTime = null;
        if (channel == null) throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);

        if (channel.getChannelType() == Channel.ChannelType.PRIVATE) {
            readTime = readStatusRepository.findUpToDateReadTimeByChannelId(channel.getId());
        }

        return ChannelDTO.response.from(EntryUtils.of(id, channel), readTime);
    }

    @Override
    public ChannelDTO.response find(UUID uuid) {
        return channelRepository.loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst()
                .map(ChannelDTO.response::from)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }


    @Override
    public List<ChannelDTO.response> findAllByUserId(UUID userId) {
        return channelRepository.findChannelsByUserId(userId).entrySet().stream()
                .map(ChannelDTO.response::from)
                .collect(Collectors.toList());
    }

    @Override
    public CommonDTO.idResponse update(ChannelDTO.update updateDTO) {
        boolean isUpdated = false;
        try {
            Channel channel = channelRepository.load(updateDTO.id());
            if (channel.getChannelType() == Channel.ChannelType.PRIVATE) {
                throw new CustomException(ErrorCode.PRIVATE_CANNOT_MODIFY);
            }
            channelRepository.update(updateDTO.id(), channel);
            return CommonDTO.idResponse.from(updateDTO.id(), channel.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 채널 삭제
    @Override
    public CommonDTO.idResponse delete(Long id) {
        ChannelDTO.response deleteChannel = find(id);

        messageRepository.deleteAllByChannelId(deleteChannel.uuid());
        readStatusRepository.deleteAllByChannelId(deleteChannel.uuid());

        channelRepository.delete(id);
        return CommonDTO.idResponse.from(deleteChannel.id(), deleteChannel.uuid());
    }

    @Override
    public CommonDTO.idResponse delete(UUID uuid) {
        ChannelDTO.response deleteChannel = find(uuid);

        messageRepository.deleteAllByChannelId(uuid);
        readStatusRepository.deleteAllByChannelId(uuid);

        channelRepository.delete(deleteChannel.id());
        return CommonDTO.idResponse.from(deleteChannel.id(), deleteChannel.uuid());
    }
}
