package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.common.error.ErrorMessage;
import com.sprint.mission.discodeit.common.error.channel.ChannelException;
import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.db.channel.ChannelRepository;
import com.sprint.mission.discodeit.db.message.ChannelMessage.ChannelMessageRepository;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.ChannelMessage;
import com.sprint.mission.discodeit.entity.message.dto.ChannelMessageInfoResponse;
import com.sprint.mission.discodeit.entity.message.dto.SendChannelMessageRequest;
import com.sprint.mission.discodeit.entity.user.entity.User;
import com.sprint.mission.discodeit.service.message.channelMessage.ChannelMessageService;
import com.sprint.mission.discodeit.service.message.converter.ChannelMessageConverter;
import java.util.UUID;

public class JCFChannelMessageService implements ChannelMessageService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ChannelMessageRepository channelMessageRepository;
    private final ChannelMessageConverter converter;

    private JCFChannelMessageService(
            UserRepository userRepository,
            ChannelRepository channelRepository,
            ChannelMessageRepository channelMessageRepository,
            ChannelMessageConverter converter
    ) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.channelMessageRepository = channelMessageRepository;
        this.converter = converter;
    }

    public static ChannelMessageService getInstance(
            UserRepository userRepository,
            ChannelRepository channelRepository
    ) {
        var converter = new ChannelMessageConverter();
        return new JCFChannelMessageService(userRepository, channelRepository, converter);
    }

    @Override
    public ChannelMessageInfoResponse sendMessage(SendChannelMessageRequest sendChannelMessageRequest) {
        // 유저 찾기
        var foundUser = findUserByIdOrThrow(sendChannelMessageRequest.sendUserId());
        // 채널 찾기
        var foundChannel = findChannelByIdOrThrow(sendChannelMessageRequest.receiveChannelId());
        // 메시지 생성
        var channelMessage = ChannelMessage.ofMessageAndSenderAndReceiverChannel(
                sendChannelMessageRequest.message(),
                foundUser,
                foundChannel
        );
        // 메시지 저장
        var savedMessage = channelMessageRepository.save(channelMessage);
        // 메시지 전송
        savedMessage.sendMessage();
        // 생성된 메시지 반환
        return converter.toDto(savedMessage);
    }

    // 이거 중복이 많이 나오는데, 유저 서비스에 넣어서 처리하는게 좋아보임
    private User findUserByIdOrThrow(UUID userId) {
        var foundUser = userRepository.findById(userId)
                .orElseThrow(() -> UserException.ofErrorMessageAndId(
                        ErrorMessage.USER_NOT_FOUND, userId.toString()
                ));

        return foundUser;
    }


    private Channel findChannelByIdOrThrow(UUID channelId) {
        var foundChannel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelException.ofErrorMessageAndNotExistChannelId(
                        ErrorMessage.CHANNEL_NOT_FOUND,
                        channelId
                ));
        return foundChannel;
    }
}
