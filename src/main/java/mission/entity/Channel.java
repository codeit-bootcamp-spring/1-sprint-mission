package mission.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Channel implements Serializable {

    private static final long serialVersionUID = 2L;

    private final UUID id;
    private final String firstId; // 콘솔창 용

    private String name;
    //private String oldName;
    private final Set<User> userList = new HashSet<>();
    private final Set<Message> messageList = new HashSet<>();
    private Integer userCount;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Channel(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        String idAsString = id.toString();
        this.firstId = idAsString.split("-")[0];
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Message> getMessageList(){
        return messageList;
    }

    public Set<User> getUsersImmutable(){
        return Collections.unmodifiableSet(userList);
    }

    // getUserList 없앤 이유 : 아래의 메서드로 userlist.remove 시 count를 초기화하기 위해
    public void removeUser(User user){
        if(userList.contains(user)){
            userList.remove(user);
            user.removeChannel(this);
            userCount = userList.size();
            updatedAt = LocalDateTime.now();
        }
    }

    // 채널이
    public void removeAllUser(){
        for (User user : userList) {
            removeUser(user);
        }
        userCount = userList.size();
    }

    public void addUser(User user){
        if(!userList.contains(user)){
            userList.add(user);
            user.addChannel(this);
            userCount = userList.size();
            updatedAt = LocalDateTime.now();
        }
    }

    public void addMessage(Message message){
        messageList.add(message);
        //updatedAt = LocalDateTime.now();
    }

    public void deleteMessage(Message message){
        messageList.remove(message);
        //updatedAt = LocalDateTime.now();
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getUserCount() {
        return userCount = userList.size();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id) && Objects.equals(firstId, channel.firstId) && Objects.equals(name, channel.name) && Objects.equals(userList, channel.userList) && Objects.equals(userCount, channel.userCount) && Objects.equals(createdAt, channel.createdAt) && Objects.equals(updatedAt, channel.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstId, name, userList, userCount, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "firstId='" + firstId + '\'' +
                ", name='" + name + '\'' +
                ", userCount=" + getUserCount() +
                '}';
    }
}