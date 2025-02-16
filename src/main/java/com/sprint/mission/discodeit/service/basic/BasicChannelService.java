package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    // 순환 참조를 방지하기 위해 UserService가 아닌 UserRepository 사용
    // 조회(Read)만 하기 때문에 순환 참조를 해결하려고 사용한 방법이지만, 단일 책임 원칙에서 벗어나는 것 같아 옳지 않은 방법이지 않나 싶습니다.
    private final UserRepository userRepository;

    //메세지의 경우 다른 곳에서 Service 를 이용해 message
    private final MessageRepository messageRepository;

    private final ReadStatusService readStatusService;

    private final UserStatusService userStatusService;

    @Override
    public ChannelResponseDto create(CreateChannelDto createChannelDto) {
        Channel channel = channelRepository.save(new Channel(createChannelDto.channelName(), createChannelDto.channelType(), createChannelDto.channelCategory(), createChannelDto.description()));
        return ChannelResponseDto.from(channel);
    }

    //private 인 경우, userId 리스트 매개변수 필요
    private ChannelResponseDto create(CreateChannelDto createChannelDto, List<String> userIds) {
        Channel channel = new Channel(null, createChannelDto.channelType(), createChannelDto.channelCategory(), null);

        for (String userId : userIds) {
            User user = userRepository.findById(userId);
            readStatusService.create(new CreateReadStatusDto(channel.getId(), userId));
            channel.getUserSet().add(user.getId());
        }
        Channel savedChannel = channelRepository.save(channel);
        return ChannelResponseDto.from(savedChannel);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(String userId) {
        List<Channel> channels = channelRepository.findAll().stream().toList();

        List<ChannelResponseDto> channelResponseDtos = new ArrayList<>();

        for(Channel channel: channels) {
            //PRIVATE 채널인데, 해당 채널에 유저가 참여하고 있지 않다면 조회 결과 담지 않음
            if(channel.getChannelType().equals(ChannelType.PRIVATE) && !channel.getUserSet().contains(userId) ) {
                continue;
            }

            Optional<Instant> lastMessageTimestamp = messageRepository.findAllByChannelId(channel.getId()).stream()
                    .map(Message::getCreatedAt)
                    .max(Instant::compareTo);

            List<String> userIds = ( channel.getChannelType() == ChannelType.PRIVATE ? channel.getUserSet().stream().toList() : null );

            if(channel.getChannelType() == ChannelType.PRIVATE) {
                channelResponseDtos.add(new ChannelResponseDto(
                        channel.getId(),
                        channel.getChannelName(),
                        channel.getChannelType(),
                        channel.getChannelCategory(),
                        channel.getDescription(),
                        lastMessageTimestamp.orElse(null),
                        userIds
                ));
            }
        }

        return channelResponseDtos;
    }

    @Override
    public ChannelResponseDto findById(String channelId) throws CustomException {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        Optional<Instant> lastMessageTimestamp = messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo);

        List<String> userIds = ( channel.getChannelType() == ChannelType.PRIVATE ? channel.getUserSet().stream().toList() : null );

        return new ChannelResponseDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getChannelType(),
                channel.getChannelCategory(),
                channel.getDescription(),
                lastMessageTimestamp.orElse(null),
                userIds
        );
    }

    @Override
    public List<ChannelResponseDto> findAllByChannelName(String channelName) throws CustomException {
        List<Channel> channels = channelRepository.findAll().stream().filter(c -> c.getChannelName().contains(channelName)).toList();
        List<ChannelResponseDto> channelResponseDtos = new ArrayList<>();
        for(Channel channel: channels) {
            channelResponseDtos.add(ChannelResponseDto.from(channel));
        }
        return channelResponseDtos;
    }

    @Override
    public List<ChannelResponseDto> findByChannelType(ChannelType channelType) {
        List<Channel> channels = channelRepository.findAll().stream().filter(c -> c.getChannelType().equals(channelType)).toList();
        List<ChannelResponseDto> channelResponseDtos = new ArrayList<>();
        for(Channel channel: channels) {
            channelResponseDtos.add(ChannelResponseDto.from(channel));
        }
        return channelResponseDtos;

    }

    @Override
    public ChannelResponseDto updateChannel(String channelId, UpdateChannelDto updateChannelDto) throws CustomException {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        } else if (updateChannelDto == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA);
        } else if (channel.getChannelType() == ChannelType.PRIVATE) {
            throw new CustomException(ErrorCode.CHANNEL_PRIVATE_NOT_UPDATABLE);
        }

        // 변경사항이 있는 경우에만 업데이트 시간 설정
        if (channel.isUpdated(updateChannelDto)) {
            channel.setUpdatedAt(updateChannelDto.updatedAt());
        }

        channelRepository.save(channel);

        return ChannelResponseDto.from(channel);
    }

    @Override
    public boolean delete(String channelId) throws CustomException {
        Channel channel = channelRepository.findById(channelId);

        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        messageRepository.findAllByChannelId(channelId).forEach(m-> messageRepository.delete(m.getId()));
        readStatusService.findAllByChannelId(channelId).forEach(rs -> readStatusService.delete(rs.getId()));

        return channelRepository.delete(channel);
    }

    @Override
    public List<UserResponseDto> findAllUserInChannel(Channel channel) throws CustomException {
        Channel ch = channelRepository.findById(channel.getId());
        if (ch == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        List<UserResponseDto> result = new ArrayList<>();
        for(String userId: ch.getUserSet()) {
            User user = userRepository.findById(userId);
            if (user == null) {
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
            }
            result.add(UserResponseDto.from(user, userStatusService.findById(user.getId()).isActive()));
        }
        return result;
    }

    @Override
    public boolean addUserToChannel(String channelId, String userId) throws CustomException {
        try {
            //해당 채널이 DB에 존재하는 채널인지 검사
            Channel c = channelRepository.findById(channelId);
            //해당 유저가 DB에 존재하는 유저인지 검사
            User u = userRepository.findById(userId);
            c.getUserSet().add(u.getId());
            channelRepository.save(c);
            return true;
        } catch (CustomException e) {
            if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
                System.out.println("Failed to add User to this channel. User with id " + userId + " not found");
            } else if (e.getErrorCode() == ErrorCode.CHANNEL_NOT_FOUND) {
                System.out.println("Failed to add User to this channel. Channel with id " + channelId + " not found");
            }
        }
        return false;
    }

    @Override
    public boolean deleteUserFromChannel(String channelId, String userId) {
        Channel channel = channelRepository.findById(channelId);
        User user = userRepository.findById(userId);
        if (channel.getUserSet().contains(user.getId())) {
            channel.getUserSet().remove(user.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserInChannel(String channelId, String userId) {
        Channel channel = channelRepository.findById(channelId);
        User user = userRepository.findById(userId);
        return channel.getUserSet().contains(user.getId());
    }
}
