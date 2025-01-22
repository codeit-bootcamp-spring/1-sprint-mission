package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public class Message {
    final private UUID id;
    final private Long createdAt;
    private Long updatedAt;
    private String title;
    private String messageBody;
    //private static final Set<UUID> existMessageIdCheck=new HashSet<>();

    private String senderName;
    private String receiverName;
    private UUID senderId;
    private UUID receiverId;

    private Message(String title,String messageBody){

        this.id = UUID.randomUUID();
        this.createdAt= System.currentTimeMillis();
        this.updatedAt=null;
        this.title=title;
        this.messageBody=messageBody;
        this.senderName="";
        this.receiverName ="";
        this.senderId=null;
        this.receiverId =null;

    };
    private Message(UUID ID,Long createdAt,Long updatedAt,String title,String messageBody,
                   String senderName,String receiverName, UUID senderId, UUID receiverId ){

        this.id=ID;

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
        return new Message(title,messageBody);
    }
    public static Message createChannelAll(UUID ID,Long createdAt,Long updatedAt,String title,String messageBody,
                                           String senderName,String receiverName, UUID senderId, UUID receiverId){
        return new Message(ID,createdAt,updatedAt,title,messageBody,senderName,receiverName,senderId,receiverId);
    }


    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }
    public String getMessageBody(){
        return messageBody;
    }

    public void setUpdatedAt() {
        this.updatedAt= System.currentTimeMillis();
    }
    public void setMessageBody(String messageBody){
        this.messageBody= messageBody;
    }

    public String getSenderName() {
        return senderName;
    }
    public String getReceiverName() {
        return senderName;
    }



    public UUID getSenderID() {
        return senderId;
    }
    public UUID getReceiverID() {
        return receiverId;
    }
    public void updateReceiverID(UUID receiverId) {
        this.receiverId = receiverId;
    }
    public void updateSenderID(UUID senderId) {
        this.senderId = senderId;
    }
    public void updateReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public void updateSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void updateSender(UUID senderId, String senderName){
        this.senderName=senderName;
        this.senderId=senderId;
    }
    public void updateReceiver(UUID receiverId, String receiverName){
        this.receiverName = receiverName;
        this.receiverId = receiverId;
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
