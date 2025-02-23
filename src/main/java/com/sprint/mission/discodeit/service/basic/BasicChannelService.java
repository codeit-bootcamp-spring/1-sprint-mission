package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel create(PublicChannelCreateRequest publicChannelCreateRequest) {
        User user = userRepository.findById(publicChannelCreateRequest.adminId())
                .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

        if (channelRepository.existsName(publicChannelCreateRequest.channelName())){
            throw new IllegalArgumentException("이미 존재하는 채널 이름입니다.");
        }

        Channel channel = new Channel(ChannelType.PUBLIC, publicChannelCreateRequest.channelName(), user);
        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        User user = userRepository.findById(privateChannelCreateRequest.adminId())
                .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

        if (!channelRepository.existsName(privateChannelCreateRequest.channelName())){
            throw new IllegalArgumentException("이미 존재하는 채널 이름입니다.");
        }

        Channel channel = new Channel(ChannelType.PRIVATE, privateChannelCreateRequest.channelName(), user);
        channel.addMember(user);
        return channelRepository.save(channel);
    }

    @Override
    public ChannelDTO find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
        return new ChannelDTO(
                channel.getId(),
                channel.getAdmin().getId(),
                channel.getType(),
                channel.getChannelName(),
                channel.getType() == ChannelType.PRIVATE ? channel.getMemberList() : List.of(),
                channel.getUpdatedAt()
                // 채널의 마지막 메시지의 업데이트 시간인데..음
        );
    }

    @Override
    public List<ChannelDTO> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC ||
                        (channel.getType() == ChannelType.PRIVATE && channel.getMemberList().contains(userId)))
                .map(channel -> new ChannelDTO(
                        channel.getId(),
                        channel.getAdmin().getId(),
                        channel.getType(),
                        channel.getChannelName(),
                        channel.getType() == ChannelType.PRIVATE ? channel.getMemberList() : List.of(),
                        channel.getUpdatedAt()
                ))
                .toList();
    }


    @Override
    public Channel update(UUID channelId, UUID adminId, PublicChannelUpdateRequest publicChannelUpdateRequest) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

        if(!channel.getAdmin().getId().equals(adminId)){
            throw new IllegalArgumentException("관리자만 수정할 수 있습니다.");
        }

        channel.update(publicChannelUpdateRequest.newName());
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId, UUID adminId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

        if (!channel.getAdmin().getId().equals(adminId)) {
            throw new IllegalArgumentException("관리자만 삭제할 수 있습니다.");
        }
        channelRepository.delete(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
    }

    @Override
    public void joinPrivateChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

        if (channel.getType().equals(ChannelType.PUBLIC)){
            throw new IllegalArgumentException("Public 채널은 누구든지 이용 가능합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

        channel.addMember(user);
        channelRepository.save(channel);
    }

    @Override
    public void leavePrivateChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

        if (channel.getType().equals(ChannelType.PUBLIC)){
            throw new IllegalArgumentException("Public 채널은 누구든지 이용 가능합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

        channel.deleteMember(user);
        channelRepository.save(channel);
    }
}
