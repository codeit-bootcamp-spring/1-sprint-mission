package discodeit.dto.user;

import java.util.UUID;

public record UpdatePhoneNumRequest(UUID userId,
                                    String newPhoneNum) {
}
