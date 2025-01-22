package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.common.error.ErrorMessage;
import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.db.message.directMessage.DirectMessageRepository;
import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.entity.message.DirectMessage;
import com.sprint.mission.discodeit.entity.message.dto.DirectMessageInfoResponse;
import com.sprint.mission.discodeit.entity.message.dto.SendDirectMessageRequest;
import com.sprint.mission.discodeit.entity.user.entity.User;
import com.sprint.mission.discodeit.service.message.converter.DirectMessageConverter;
import com.sprint.mission.discodeit.service.message.directMessage.DirectMessageService;
import java.util.UUID;

public class JCFDirectMessageService implements DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository;
    private final DirectMessageConverter directMessageConverter;

    private JCFDirectMessageService(
            DirectMessageRepository directMessageRepository,
            UserRepository userRepository,
            DirectMessageConverter directMessageConverter
    ) {
        this.directMessageRepository = directMessageRepository;
        this.userRepository = userRepository;
        this.directMessageConverter = directMessageConverter;
    }

    public static DirectMessageService getInstance(
            DirectMessageRepository directMessageRepository,
            UserRepository userRepository
    ) {
        var converter = new DirectMessageConverter();
        return new JCFDirectMessageService(directMessageRepository, userRepository, converter);
    }

    @Override
    public DirectMessageInfoResponse sendMessage(SendDirectMessageRequest sendDirectMessageRequest) {

        var foundSendUser = findUserByIdOrThrow(sendDirectMessageRequest.sendUserId());
        var foundReceiverUser = findUserByIdOrThrow(sendDirectMessageRequest.receiveUserId());

        var createdDirectMessage = DirectMessage.ofMessageAndSenderReceiver(
                sendDirectMessageRequest.message(),
                foundSendUser,
                foundReceiverUser
        );

        var savedDirectMessage = directMessageRepository.save(createdDirectMessage);

        createdDirectMessage.sendMessage();

        return directMessageConverter.toDto(savedDirectMessage);
    }

    private User findUserByIdOrThrow(UUID userId) {
        var foundUser = userRepository.findById(userId)
                .filter(User::isNotUnregistered)
                .orElseThrow(() -> UserException.ofErrorMessageAndId(
                        ErrorMessage.USER_NOT_FOUND, userId.toString()
                ));

        return foundUser;
    }
}
