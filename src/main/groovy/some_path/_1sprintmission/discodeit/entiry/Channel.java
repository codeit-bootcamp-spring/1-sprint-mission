package some_path._1sprintmission.discodeit.entiry;

import lombok.Getter;
import lombok.Setter;
import some_path._1sprintmission.discodeit.entiry.enums.ChannelType;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class Channel extends BaseEntity implements Serializable {
    private String name;
    private Integer maxPerson; //최대 인원 제한
    private Set<User> users; // 채널에 속한 유저 목록
    private ChannelType type;
    private List<Message> messages; // 채널 내 메시지 목록
    private String inviteCode;

    public Channel(String name, ChannelType type) {
        super();
        this.name = name;
        this.maxPerson = 3;
        this.type = type;
        this.users = new HashSet<>();
        this.messages = new ArrayList<>();
        this.inviteCode = UUID.randomUUID().toString();
    }

    public void addMember(User user) {
        if (users.size() >= maxPerson) {
            throw new IllegalStateException("Maximum number of members reached. Cannot add more members.");
        }
        if (!users.contains(user)) {
            users.add(user);
        } else {
            throw new IllegalArgumentException("User is already a member of the channel.");
        }
    }

    public void removeMember(User user) {
        if (users.contains(user)) {
            users.remove(user);
        } else {
            throw new IllegalArgumentException("User is not a member of the channel.");
        }
    }

    public void update(String newName) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            super.setUpdatedAt(Instant.from(LocalDateTime.now()));
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + getId().toString() +
                ", name='" + name + '\'' +
                ", users=" + users.stream().map(User::getUsername).toList() +
                ", maxPerson='" + maxPerson + '\'' +
                '}';
    }
}