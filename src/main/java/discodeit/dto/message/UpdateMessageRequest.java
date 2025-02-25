package discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record UpdateMessageRequest(UUID messageId,
                                   String newMessageDetail,
                                   List<UUID> attachmentIds) {
}
