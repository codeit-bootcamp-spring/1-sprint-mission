package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.entity.BinaryContent.setUpBinaryContent;
import static com.sprint.mission.discodeit.entity.UserStatus.setUpUserStatus;

@Getter
@Setter
public class User implements Comparable<User>{
    final private UUID id;
    final  private Instant createdAt;
    private Instant updatedAt;

    private String name;
    private long password;
    private String email;
    private BinaryContent selfImg;
    private UserStatus userStatus;
    private List<Message> userMessage;


    private User(String name,String password,String email){
        this.id=UUID.randomUUID();
        this.createdAt=Instant.now();
        this.updatedAt=null;
        this.name=name;
        this.password=convertString(password);
        this.email=email;
        this.selfImg=setUpBinaryContent();
        this.userStatus=setUpUserStatus(this.id);
        this.userMessage=new ArrayList<>();
    };

    private User(UUID id, Instant createdAt, Instant updatedAt, String name,long password, String email,
                 BinaryContent selfImg,UserStatus userStatus,List<Message> userMessage){
        this.id=id;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.name=name;
        this.password=password;
        this.email=email;
        this.selfImg=selfImg;
        this.userStatus=userStatus;
        this.userMessage=userMessage;
    }

    public static User createDefaultUser(String name,String password,String email){
        return new User(name,password,email);
    }
    public static User createUserAll(UUID id, Instant createdAt, Instant updatedAt, String name,
                                     long password, String email,BinaryContent selfImg,UserStatus userStatus
            ,List<Message> userMessage){
        return new User( id,  createdAt,  updatedAt,  name, password,  email, selfImg,userStatus,userMessage);
    }


    public void updateUpdatedAt() {
        this.updatedAt= Instant.now();
            }
            public void updateName(String name){
                this.name= name;
            }

    public boolean checkPassword(String password){
        return this.password == (convertString(password));
    }


    public void updateBinaryContent(char [] img){
        this.selfImg=selfImg.makeNewBinaryContent(img);
    }



    public void addMessageToUser(Message m){
        this.userMessage.add(m);
    }

    public List<UUID> getUserMessageId(){
        return userMessage.stream()
                .map(Message::getId)
                .collect(Collectors.toList());
    }

    @Override
    public String toString(){
        return "ID: "+getId()+" Name: "+getName()+" email: "+email+" createdAt: "+getCreatedAt()+
                " updatedAt: "+(getUpdatedAt() == null ? "없음" : String.valueOf(getUpdatedAt()))+"\n";
    }

    private long convertString(String str) {
        if (str.length() > 8) {
            str = str.substring(0, 8);
        }

        StringBuilder sb = new StringBuilder();
        int mask = 921;

        for (char c : str.toCharArray()) {
            int encryptedChar = c ^ mask;
            String binaryString = String.format("%8s", Integer.toBinaryString(encryptedChar))
                    .replace(' ', '0');
            sb.append(binaryString);
        }

        while (sb.length() < 32) {
            sb.append("0");
        }

        if (sb.length() > 64) {
            sb.setLength(64);
        }

        return Long.parseUnsignedLong(sb.toString(), 2);
    }



    @Override
    public int compareTo(User other) {
        return this.id.compareTo(other.getId()); // 사용자 이름 기준 정렬
    }


}
