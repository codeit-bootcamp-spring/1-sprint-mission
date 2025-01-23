package discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String name;
    private String introduction;
    private User owner;
    private List<User> participants;
    private List<Message> messages;

    public Channel(String name, String introduction, User owner) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        this.id = UUID.randomUUID();
        this.createdAt = currentUnixTime;
        this.updatedAt = currentUnixTime;

        this.name = name;
        this.introduction = introduction;
        this.owner = owner;
        this.participants = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis() / 1000;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void update(String name, String introduction) {
        boolean updated = updateName(name) || updateIntroduction(introduction);
        if (updated) {
            updateUpdatedAt();
        }
    }

    public boolean updateName(String name) {
        if (this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

    public boolean updateIntroduction(String introduction) {
        if (this.introduction.equals(introduction)) {
            return false;
        }
        this.introduction = introduction;
        return true;
    }

    public void updateParticipants(User user) {
        isDuplicateUser(user);
        participants.add(user);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void updateMessages(Message message) {
        messages.add(message);
    }

    public void deleteParticipant(User user) {
        if (!participants.contains(user)) {
            throw new IllegalArgumentException("삭제할 유저를 찾을 수 없습니다.");
        }
        participants.remove(user);
    }

    public void deleteAllParticipants(User user) {
        if (!owner.isEqualTo(user)) {
            throw new IllegalArgumentException("채널 삭제는 방장만 가능합니다.");
        }
        owner.deleteJoinedChannel(this);
        participants.forEach(participant -> participant.deleteJoinedChannel(this));
        participants.clear();
    }

    public void deleteMessage(Message message, User user) {
        if (!messages.contains(message)) {
            throw new IllegalArgumentException("삭제할 메시지를 찾을 수 없습니다.");
        }
        message.checkSender(user);
        messages.remove(message);
    }

    public void deleteAllMessages() {
        messages.clear();
    }

    public boolean isIdEqualTo(UUID id) {
        return this.id.equals(id);
    }

    public void isDuplicateUser(User user) {
        if (participants.contains(user)) {
            throw new IllegalArgumentException("이미 가입된 유저입니다.");
        }
    }

    @Override
    public String toString() {
        return String.format(
                name + " | " + introduction + System.lineSeparator()
                        + "Owner: " + owner.getName() + System.lineSeparator()
                        + "Participants: "
                        + String.join(", ", participants.stream()
                        .map(User::getName)
                        .toList()) + System.lineSeparator()
                        + "-- Messages -- " + System.lineSeparator()
                        + String.join(System.lineSeparator(), messages.stream()
                        .map(Message::toString).toList())
        );
    }
}
