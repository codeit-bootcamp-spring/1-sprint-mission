package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Message {
    final private UUID id;
    final private Instant createdAt;
    private Instant updatedAt;
    @Setter
    private String title;
    @Setter
    private String messageBody;
    private final List<BinaryContent> binaryContents;

    private String senderName;
    private String receiverName;
    private UUID senderId;
    private UUID receiverId;

    private Message(String title,String messageBody,List<BinaryContent> binaryContents){

        this.id = UUID.randomUUID();
        this.createdAt= Instant.now();
        this.updatedAt=null;
        this.title=title;
        this.messageBody=messageBody;
        this.senderName="";
        this.receiverName ="";
        this.senderId=null;
        this.receiverId =null;
        this.binaryContents=binaryContents;

    };
    private Message(UUID id, Instant createdAt, Instant updatedAt, String title, String messageBody, List<BinaryContent> binaryContents,
                    String senderName, String receiverName, UUID senderId, UUID receiverId ){

        this.id=id;
        this.binaryContents = binaryContents;

        this.createdAt= createdAt;
        this.updatedAt=updatedAt;
        this.title=title;
        this.messageBody=messageBody;
        this.senderName=senderName;
        this.receiverName =receiverName;
        this.senderId=senderId;
        this.receiverId =receiverId;

    };

    public static Message createDefaultMessage(String title,String messageBody){
         ArrayList<BinaryContent> newBinary=new ArrayList<>();
        return new Message(title,messageBody,newBinary);
    }
    public static Message createDefaultMessage(String title,String messageBody,List<BinaryContent> binaryContents){
        return new Message(title,messageBody,binaryContents);
    }
    public static Message createChannelAll(UUID Id, Instant createdAt, Instant updatedAt, String title, String messageBody, ArrayList<BinaryContent> binaryContents,
                                           String senderName, String receiverName, UUID senderId, UUID receiverId){
        return new Message(Id,createdAt,updatedAt,title,messageBody,binaryContents,senderName,receiverName,senderId,receiverId);
    }


    public void setUpdatedAt() {
        this.updatedAt= Instant.now();
    }


    public void updateSender(UUID senderId, String senderName){
        this.senderName=senderName;
        this.senderId=senderId;
    }
    public void updateReceiver(UUID receiverId, String receiverName){
        this.receiverName = receiverName;
        this.receiverId = receiverId;
    }

    public List<UUID> getBinaryConetentIdList(){
        return binaryContents.stream()
                .map(BinaryContent::getId)
                .collect(Collectors.toList());
    }
    @Override
    public String toString(){
        StringBuilder display = new StringBuilder();
        display.append("메세지 아이디: ").append(id)
                .append(" createdAt: ").append(createdAt)
                .append(" updatedAt: ").append(getUpdatedAt() == null ? "없음" : String.valueOf(getUpdatedAt()))
                .append("\nsenderName: ").append(senderName)
                .append(" senderID: ").append(senderId)
                .append("\nReceiverName: ").append(receiverName)
                .append(" ReceiverID: ").append(receiverId)
                .append("\n제목: ").append(title)
                .append(" Message: ").append(messageBody)
                .append("\n");
        return display.toString();
    }

}
