package some_path._1sprintmission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.enums.ChannelType;

import java.time.Instant;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ChannelDTO {
    private UUID channelId;
    private String name;
    private ChannelType type;
    private Instant lastMessageTime; // 가장 최근 메시지 시간
    private Set<UUID> userIds; // 참여한 유저들의 ID

    // Channel 객체를 DTO로 변환
    public static ChannelDTO from(Channel channel) {
        Instant lastMessageTime = channel.getMessages().stream()
                .map(Message::getSentAt)
                .max(Comparator.naturalOrder())
                .orElse(null); // 메시지가 없다면 null

        Set<UUID> userIds = null;
        if (channel.getType() == ChannelType.PRIVATE) {
            // PRIVATE 채널인 경우에만 user ID를 가져옴
            userIds = channel.getUsers().stream()
                    .map(user -> user.getId())
                    .collect(Collectors.toSet());
        }

        return new ChannelDTO(
                channel.getId(),
                channel.getName(),
                channel.getType(),
                lastMessageTime,
                userIds
        );
    }
}
