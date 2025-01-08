package sprint.mission.discodeit.service.jcf;

import sprint.mission.discodeit.entity.Message;
import sprint.mission.discodeit.service.MessageService;
import sprint.mission.discodeit.validation.MessageValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data; // assume that it is repository
    private final MessageValidator   messageValidator;

    private JCFMessageService() {
        data             = new HashMap<>();
        messageValidator = MessageValidator.getInstance();
    }

    private static final class InstanceHolder {
        private final static JCFMessageService INSTANCE = new JCFMessageService();
    }

    public static JCFMessageService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Message create(Message userToCreate) {
        messageValidator.validate(userToCreate);
        UUID key = userToCreate.getCommon().getId();
        return Optional.ofNullable(data.putIfAbsent(key, userToCreate))
                .map(existingMessage -> Message.createEmptyMessage())
                .orElse(userToCreate);
    }

    @Override
    public Message read(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(Message.createEmptyMessage());
    }

    @Override
    public Message update(UUID key, Message messageToUpdate) {
        messageValidator.validate(messageToUpdate);
        return Optional.ofNullable(data.computeIfPresent(
                        key, (id, user)-> messageToUpdate))
                .orElse(Message.createEmptyMessage());
    }

    @Override
    public Message delete(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(Message.createEmptyMessage());
    }
}
