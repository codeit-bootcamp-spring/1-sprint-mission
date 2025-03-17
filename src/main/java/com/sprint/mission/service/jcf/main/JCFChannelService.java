package com.sprint.mission.service.jcf.main;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.ChannelMapper;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.BaseEntity;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;

import com.sprint.mission.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static com.sprint.mission.entity.main.ChannelType.PRIVATE;
import static com.sprint.mission.entity.main.ChannelType.PUBLIC;


@Slf4j
@Service
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;
    private final JCFUserService userService;
    private final MessageRepository messageRepository;


    @Override
    public Channel createPublicChannel(PublicChannelCreateDTO request) {
        log.info("createPublicChannel = {}", request);
        return channelRepository.save(channelMapper.toPublicEntity(request, PUBLIC));
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateDTO request) {
        Channel createdChannel = channelRepository.save(channelMapper.toPrivateEntity(PRIVATE));
        request.participantIds().stream()
                .map(userId -> {
                    User participatingUser = userRepository.findById(userId)
                            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
                    return new ReadStatus(participatingUser, createdChannel, createdChannel.getCreatedAt());
                })// 나중에
                .forEach(readStatusRepository::save);
        return createdChannel;
    }


    /**
     * [ ] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId [ ] PUBLIC 채널
     * 목록은 전체 조회합니다. [ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.
     */
    @Override
    public Channel findById(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    //
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        // 쿼리1
        List<ReadStatus> readStatusList = readStatusRepository.findAllByUser_Id(userId);
        // 유저가 참여한 Private 채널 리스트
        List<Channel> participatingPrivateChannel = readStatusList.stream().map(ReadStatus::getChannel).toList();

        List<ChannelDto> channelDtoList = new ArrayList<>();
        participatingPrivateChannel.forEach((channel)->{
            // 한 채널의 ReadStauts들 가져오기
            // 쿼리2
            List<ReadStatus> channelReadStatus = readStatusRepository.findAllByChannel_Id(channel.getId());
            // 쿼리3
            Instant lastMessageAt = messageRepository.findTop1ByChannel_IdOrderByCreatedAtDesc(channel.getId())
                    .map(BaseEntity::getCreatedAt)
                    .orElseGet(null);
            List<User> userList = channelReadStatus.stream().map(ReadStatus::getUser).toList();
            channelDtoList.add(channelMapper.toDto(channel, userList, lastMessageAt));
        });

        // 쿼리4
        List<Channel> publicChannel = channelRepository.findAllByChannelType(PUBLIC);
        publicChannel.forEach((channel) -> channelDtoList.add(channelMapper.toDto(channel)));

        return channelDtoList;
    }


    //public record ChannelDto(
    //        UUID id,
    //        ChannelType channelType,
    //        String name,
    //        String description,
    //        List<UserDto> participants,
    //        Instant lastMessageAt) {
    //}

    @Override
    public Channel update(UUID channelId, ChannelDtoForUpdate dto) {
        Channel updatingChannel = this.findById(channelId);
        if (updatingChannel.isPrivate()) {
            throw new CustomException(ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL);
        }
        updatingChannel.update(dto.name(), dto.description());
        return updatingChannel;
    }

    @Override
    public void delete(UUID channelId) {
        Channel deletingChannel = this.findById(channelId);

        if (deletingChannel.isPrivate()) {
            readStatusRepository.deleteAllByChannel(deletingChannel);
        }
        messageService.deleteAllByChannelId(channelId);
        channelRepository.delete(deletingChannel);
    }

    /**
     * 중복 검증
     */
    public void validateDuplicateName(String name) {
        boolean isDuplicate = channelRepository.findAll().stream()
                .anyMatch(channel -> channel.getName().equals(name));
        if (isDuplicate) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_NAME);
        }
    }

    /**
     * 응답 DTO (타입별)
     */
//    private FindChannelDto getFindChannelDto(Channel findedChannel) {
//        return (findedChannel.getChannelType().equals(ChannelType.PRIVATE)
//                ? new FindPrivateChannelDto(findedChannel)
//                : new FindPublicChannelDto(findedChannel));
//    }
//
//    // 카피 해온거
//    private FindChannelAllDto toDto(Channel channel) {
//        Instant lastMessageAt = messageService.findAllByChannelId(channel.getId())
//                .stream()
//                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
//                .map(Message::getCreatedAt)
//                .limit(1)
//                .findFirst()
//                .orElse(Instant.MIN);
//
//        List<UUID> participantIds = new ArrayList<>();
//        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
//            readStatusRepository.findAllByChannelId(channel.getId()).stream()
//                    .map((readStatus) -> readStatus.getUser().getId())
//                    .forEach(participantIds::add);
//        }
//
//        return new FindChannelAllDto(
//                channel.getId(),
//                channel.getChannelType(),
//                channel.getName(),
//                channel.getDescription(),
//                participantIds,
//                lastMessageAt
//        );
//    }

//        public Map<FindUserDto, Instant> lastReadTimeListInChannel(UUID channelId) {
//            Channel inChannel = channelRepository.findById(channelId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));
//            if (inChannel.getChannelType().equals(ChannelType.PUBLIC)) {
//                throw new CustomException(ErrorCode.CANNOT_REQUEST_LAST_READ_TIME);
//            }
//
//            // 유저별 이 채널 마지막 readTime
//            // 이 때 쓰는 findUserDto는 profile null
//            Map<FindUserDto, Instant> readTimeMap = new HashMap<>();
//            for (User user : inChannel.getUserList()) {
//                UserStatus status = userStatusService.findById(user.getId());
//                readTimeMap.put(new FindUserDto(user, status.isOnline()), user.getReadStatus().findLastReadByChannel(channelId));
//            }
//            return readTimeMap;
//        }
}

