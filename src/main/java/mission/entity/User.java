package mission.entity;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String firstId;

    private String name;
    private String password;
    private final LocalDateTime createAt;
    private LocalDateTime updateAt;

    private final Set<Channel> channels = new HashSet<>();
    private final Set<Message> messages = new HashSet<>();

    // 이 User가 입력한 모든 메시지
    public Set<Message> getMessages() {
        return messages;
    }

    //  전송된 메시지 취소
    public UUID deleteMessage(Message message){
        messages.remove(message);
        return message.getId();
    }

    // User가 메시지 수정
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

    public Set<Channel> getChannelsImmutable(){
        return Collections.unmodifiableSet(channels);
    }

    public void addChannel(Channel channel){
        if(!channels.contains(channel)){
            channels.add(channel);
            channel.addUser(this);
            updateAt = LocalDateTime.now();
        }
        // 굳이 if 문 만든 이유 : 불필요한 updateAt 초기화 없애기 위해
    }

    // User가 채널 삭제
    public void removeChannel(Channel channel) {
        if(channels.contains(channel)){
            channels.remove(channel);
            channel.removeUser(this);
            updateAt = LocalDateTime.now();
        }
    }

    public void removeAllChannel(){
        for (Channel channel : channels) {
            removeChannel(channel);
            // 유저수 초기화하려면 channels.clear 로는 안되기 때문
        }
        updateAt = LocalDateTime.now();
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
    public String getFirstId(){
        return firstId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
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
        this.password = password;
        this.name = name;
        updateAt = LocalDateTime.now();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password);
    }

    @Override
    public String toString() {
        return "[" + firstId + "]" + "'" + name + "'";
    }


}
