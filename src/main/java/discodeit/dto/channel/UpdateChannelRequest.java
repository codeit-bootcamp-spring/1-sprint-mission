package discodeit.dto.channel;

import java.util.Optional;
import java.util.UUID;

// only PUBLIC channel
public record UpdateChannelRequest(UUID id,
                                   String newName,
                                   String newDescription) {
}
