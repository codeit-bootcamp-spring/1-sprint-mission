package some_path._1sprintmission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelUpdateDTO {
    private UUID channelId; // 수정할 채널 ID
    private String name; // 변경할 채널 이름
    private Integer maxPerson; // 변경할 최대 인원 수
}
