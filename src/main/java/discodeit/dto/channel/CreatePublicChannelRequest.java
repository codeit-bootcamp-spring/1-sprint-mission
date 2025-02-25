package discodeit.dto.channel;

import java.util.Optional;
import java.util.UUID;

public record CreatePublicChannelRequest(String name,
                                         String description,
                                         Optional<UUID> participants) {
}
