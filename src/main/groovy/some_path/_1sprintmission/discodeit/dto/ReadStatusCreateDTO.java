
package some_path._1sprintmission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReadStatusCreateDTO {
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
}

