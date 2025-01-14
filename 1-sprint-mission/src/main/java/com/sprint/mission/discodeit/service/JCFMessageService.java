package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.*;

public class JCFMessageService implements MessageService{
    private final Map<UUID,Message> MessageList=new TreeMap<>();

    @Override
    public void CreateMessageDefault(String title,String body) {
        if (find_Message(title).isEmpty()) {
            Message new_Message=Message.CreateDefaultMessage(title,body);
            MessageList.put(new_Message.getId(),new_Message);
        } else {
            System.out.println("이미 존재하는 제목입니다.");
        }

    }

    @Override
    public <T>String ReadMessage(T key) {
        StringBuilder list= new StringBuilder();

        TreeMap<UUID,Message> foundMessage=new TreeMap<>();

        if (key instanceof String) {
            foundMessage = find_Message((String) key);
        } else if (key instanceof UUID) {
            foundMessage = find_Message((UUID) key);
        }

        for (Message val : foundMessage.values()) {
            list.append(val.toString());
        }

        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 메세지가 없습니다.";
    }


    @Override
    public String ReadMessageAll() {
        StringBuilder list= new StringBuilder();
        for (Message val : MessageList.values()) {
            list.append(val.toString());
        }
        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "등록된 메세지가 없습니다.";
    }

    @Override
    public boolean UpdateMessageTitle(UUID ID, String change) {
        TreeMap<UUID,Message> instance = find_Message(ID);
        return ChangeMessageTitle(instance,change);
    }

    @Override
    public boolean UpdateMessageTitle(String title, String change) {
        TreeMap<UUID,Message> instance = find_Message(title);
        return ChangeMessageTitle(instance,change);
    }
    @Override
    public boolean UpdateMessageBody(UUID ID, String change) {
        TreeMap<UUID,Message> instance = find_Message(ID);
        return ChangeMessageBody(instance,change);
    }

    @Override
    public boolean UpdateMessageBody(String title, String change) {
        TreeMap<UUID,Message> instance = find_Message(title);
        return ChangeMessageBody(instance,change);
    }

    @Override
    public boolean DeleteMessage(UUID id) {
        TreeMap<UUID, Message> instance = find_Message(id);
        return DeleteMessageInfo(instance);
    }

    @Override
    public boolean DeleteMessage(String title) {
        TreeMap<UUID, Message> instance = find_Message(title);
        return DeleteMessageInfo(instance);
    }


    private boolean DeleteMessageInfo(TreeMap<UUID, Message> instance) {
        if (instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Message find = instance.firstEntry().getValue();
            find.deleteExistMessageId();
            MessageList.remove(find.getId());
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        } else {
            System.out.println("중복된 메세지가 있습니다.");
            return false;
        }
    }

    private TreeMap<UUID,Message> find_Message(UUID id) {
        TreeMap<UUID, Message> findMessage = new TreeMap<>();
        findMessage.put(id,MessageList.get(id));
        return findMessage;
    }


    private TreeMap<UUID,Message> find_Message(String title) {
        TreeMap<UUID, Message> findMessage = new TreeMap<>();
        for(Message message:MessageList.values()){
            if(message.getTitle().equals(title)){
                findMessage.put(message.getId(),message);
            }
        }
        return findMessage;
    }



    private boolean ChangeMessageTitle(TreeMap<UUID, Message> instance, String changetitle) {
        if (instance==null||instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Message find = instance.firstEntry().getValue();
            find.setTitle(changetitle);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }
        return false;
    }

    private boolean ChangeMessageBody(TreeMap<UUID, Message> instance, String changeBody) {
        if (instance==null||instance.isEmpty())  {
            System.out.println( "해당하는 메세지가가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Message find = instance.firstEntry().getValue();
            find.setMessageBody(changeBody);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            return false;
        }

    }

    @Override
    public void AddMessage(Message m) {
        MessageList.put(m.getId(),m);
    }


}
