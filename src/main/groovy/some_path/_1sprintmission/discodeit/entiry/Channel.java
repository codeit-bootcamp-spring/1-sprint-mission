package some_path._1sprintmission.discodeit.entiry;

import java.util.*;

public class Channel extends BaseEntity {
    private String name;
    private Integer limitPerson; //최대 인원 제한
    private Set<User> users; // 채널에 속한 유저 목록
    private List<Message> messages;        // 채널 내 메시지 목록

    public Channel(String name) {
        super();
        this.name = name;
        this.limitPerson = 3;
        this.users = new HashSet<>();
        this.messages = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public Set<User> getMembers() {
        return users;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages); // 읽기 전용 리스트 반환
    }

    public void addMember(User user) {
        if (users.size() >= limitPerson) {
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

    public void addMessage(Message message) {
        messages.add(message); // 메시지를 저장
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + getId().toString() +
                ", name='" + name + '\'' +
                ", users=" + users.stream().map(User::getUsername).toList() +
                '}';
    }

    public void updateName(String newName) {
        this.name = newName;
    }
}