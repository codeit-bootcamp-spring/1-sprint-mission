package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ErrorMessage;
import com.sprint.mission.discodeit.common.UtilMethod;
import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.response.CreateChannelResponse;
import com.sprint.mission.discodeit.dto.channel.response.FindChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

//    @Qualifier("fileChannelRepository")
    @Qualifier("jcfChannelRepository")
    private final ChannelRepository channelRepository;
    private final UserValidator userValidator;
    private final ChannelMapper channelMapper;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelValidator channelValidator;
    private final MessageRepository messageRepository;

    @Override
    public CreateChannelResponse createPublicChannel(CreatePublicChannelRequest createPublicChannelRequest) {
        User channelOwner = userValidator.validateUserExistsByUserId(createPublicChannelRequest.channelOwnerId());

        Channel channel = channelMapper.toEntity(createPublicChannelRequest.name(),
                createPublicChannelRequest.description(), channelOwner, ChannelType.PUBLIC);

        channelRepository.saveChannel(channel);
        return channelMapper.toCreateChannelResponse(channel);
    }

    @Override
    public CreateChannelResponse createPrivateChannel(CreatePrivateChannelRequest createPrivateChannelRequest) {
        User channelOwner = userValidator.validateUserExistsByUserId(createPrivateChannelRequest.channelOwnerId());

        // 요구 사항에 name, description 속성 생략 -> 임의 랜던값 지정
        String name = generateRandomString();
        String description = "description";

        Channel channel = channelMapper.toEntity(name, description, channelOwner, ChannelType.PRIVATE);

        // 유저 초대 및 ReadStatus 생성
        for (UUID userId : createPrivateChannelRequest.channelUserList()) {
            User user = userValidator.validateUserExistsByUserId(userId);
            ReadStatus readStatus = ReadStatus.of(user, channel);
            readStatusRepository.saveReadStatus(readStatus);

            channel.addChannelUser(user);
        }

        return channelMapper.toCreateChannelResponse(channel);
    }

    @Override
    public FindChannelResponse findChannelByIdOrThrow(UUID channelId) {
        Channel channel = channelValidator.validateChannelExistsByChannelId(channelId);

        // 가장 최근 메세지의 시간 정보(createdAt)
        Message foundMessage = messageRepository.findAllMessagesByChannel(channel).stream()
                .max(Comparator.comparing(message -> message.getCreatedAt()))
                .orElse(null);

        // 채널에 메세지가 하나도 없을 때 시간 정보를 null로 해서 보내도 될까? -> 일단 EPOCh 로 기본값 지정
        Instant lastMessageTime = Instant.EPOCH;
        if (foundMessage != null) {
           lastMessageTime = foundMessage.getCreatedAt();
        }

        List<UUID> channelUsersIdList = new ArrayList<>();

        if (channel.isPrivate()) {
            channelUsersIdList = channel.getChannelUserList().stream()
                    .map(user -> user.getId())
                    .toList();
        }

        return channelMapper.toFindChannelResponse(channel, lastMessageTime, channelUsersIdList);
    }

    @Override
    public List<Channel> findAllChannels() {
        return channelRepository.findAllChannels();
    }

    @Override
    public Channel updateChannelName(UUID channelOwnerId, UUID channelId, String name) {
        Channel foundChannel = channelValidator.validateChannelExistsByChannelId(channelId);
        userValidator.validateUserExistsByUserId(channelOwnerId);

        if (foundChannel.isNotOwner(channelOwnerId)) {
            throw new RuntimeException(ErrorMessage.NOT_CHANNEL_CREATOR.format(channelOwnerId));
        }

        foundChannel.updateName(name);
        foundChannel.updateUpdatedAt(UtilMethod.getCurrentTime());

        return channelRepository.saveChannel(foundChannel);
    }

    @Override
    public void deleteChannel(UUID channelOwnerId, UUID channelId) {
        userValidator.validateUserExistsByUserId(channelOwnerId);
        Channel foundChannel = channelValidator.validateChannelExistsByChannelId(channelId);

        if (foundChannel.isNotOwner(channelOwnerId)) {
            throw new RuntimeException(ErrorMessage.NOT_CHANNEL_CREATOR.format(channelOwnerId));
        }

        channelRepository.removeChannel(channelId);
    }

    @Override
    public Channel inviteUsers(UUID channelId, List<User> invitedUserList) {
        Channel foundChannel = channelValidator.validateChannelExistsByChannelId(channelId);

        invitedUserList.forEach(user -> foundChannel.addChannelUser(user));

        return channelRepository.saveChannel(foundChannel);
    }

    @Override
    public Channel leaveUsers(UUID channelId, List<User> leaveUserList) {
        Channel foundChannel = channelValidator.validateChannelExistsByChannelId(channelId);

        leaveUserList.forEach(user -> foundChannel.deleteChannelUser(user));

        return channelRepository.saveChannel(foundChannel);
    }

    private String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
