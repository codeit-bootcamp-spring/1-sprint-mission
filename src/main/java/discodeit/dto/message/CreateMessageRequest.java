package discodeit.dto.message;

import java.util.UUID;

public record CreateMessageRequest(String messageDetail,
                                   UUID userId,
                                   UUID channelId) {
}
