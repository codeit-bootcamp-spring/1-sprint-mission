package discodeit.dto.user;

import java.util.UUID;

public record UpdatePasswordRequest(UUID userId,
                                    String oldPassword,
                                    String newPassword) {
}
