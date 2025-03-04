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
  private Instant lastMessageTime;
  private Set<UUID> userIds;


  public static ChannelDTO from(Channel channel) {
    Instant lastMessageTime = channel.getMessages().stream()
        .map(Message::getSentAt)
        .max(Comparator.naturalOrder())
        .orElse(null);

    Set<UUID> userIds = null;
    if (channel.getType() == ChannelType.PRIVATE) {
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
