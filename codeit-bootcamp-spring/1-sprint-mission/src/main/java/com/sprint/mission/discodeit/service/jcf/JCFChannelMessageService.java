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
            ChannelRepository channelRepository,
            ChannelMessageRepository channelMessageRepository
    ) {
        var converter = new ChannelMessageConverter();
        return new JCFChannelMessageService(userRepository, channelRepository, channelMessageRepository, converter);
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

    /**
     * ==> 코드리뷰 부탁드립니다. 유저 서비스에 다른 서비스에서 이용하기 위한 메서드를 넣는게 바람직한가요?
     * ==> 제 생각은, 유저 서비스에 같은 기능으로 호출하는 기능이 필요하다면 추가하지만, 없다면 안넣는게 낫다고 생각함. 넣는다면 userRepository
     *      그런데, 유저 서비스에서 id로만 찾는 유저를 찾는 기능이 있는가? 구현한 내용은 유저 서비스 레이어 안에서만 private 구현
     *      코드 중복이 너무 많이 발생함, 이를 해결하기 위해 userService, userRepository 둘 중 하나에 메서드를 만드는 방법 중 어디가 좋을까요?
     *
     * ==> 다른 서비스 레이어를 의존하도록 해서 호출해도 괜찮을까요 ?
     */
    private User findUserByIdOrThrow(UUID userId) {
        var foundUser = userRepository.findById(userId)
                .filter(User::isNotUnregistered)
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
