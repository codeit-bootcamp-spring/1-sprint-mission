package discodeit.dto.user;

import java.util.UUID;

public record LoginRequest(UUID userId,
                           String username,
                           String password) {
}
