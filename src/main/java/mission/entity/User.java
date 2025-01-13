package mission.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private String oldName;
    private final String firstId;
    private String password;
    private final LocalDateTime createAt;
    private LocalDateTime updateAt;

    private final List<Channel> channels = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();

    // 이 User가 입력한 모든 메시지
    public List<Message> getMessages() {
        return messages;
    }

    //  전송된 메시지 취소
    public UUID deleteMessage(Message message){
        messages.remove(message);
        return message.getId();
    }

    // 메시지 수정 ==> d이거는 근데.... Message에서해야하나
    public void updateMessage(Message message, String updateMessage){
        message.setMessage(updateMessage);
    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
        id = UUID.randomUUID();
        firstId = id.toString().split("-")[0];
        createAt = LocalDateTime.now();
    }

    public List<Channel> getChannels() {
        return channels;
    }

    // 채널 명으로 검색
    public Channel getChannelByName(String channelName) {
        for (Channel channel : channels) {
            if (channel.getName().equals(channelName)){
                return channel;
            }
        }
        return null;
    }

    // User가 채널 등록
    public void addChannel(Channel channel) {
        channels.add(channel);
        channel.getUserList().add(this);
    }

    // User가 채널 삭제
    public void removeChannel(Channel channel) {
        // 존재하는지 검증?
        channels.remove(channel);
        channel.getUserList().remove(this);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setName(String name) {
        updateAt = LocalDateTime.now();
        this.name = name;
    }

    public void setPassword(String password) {
        updateAt = LocalDateTime.now();
        this.password = password;
    }

    public User setNamePassword(String name, String password) {
        updateAt = LocalDateTime.now();
        this.password = password;
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(createAt, user.createAt) && Objects.equals(updateAt, user.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, createAt, updateAt);
    }

    @Override
    public String toString() {
        return "[" + firstId + "]" + "'" + name + "'";
    }


}
