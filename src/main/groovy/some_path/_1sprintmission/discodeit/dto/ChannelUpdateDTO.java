package some_path._1sprintmission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelUpdateDTO {

    private UUID channelId;
    private String name;
    private Integer maxPerson;
}
