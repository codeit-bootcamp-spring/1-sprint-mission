package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService{
    private final List<Message> MessageList=new ArrayList<>();

    @Override
    public void CreateMessageDefault(String title,String body) {
        if (find_Message(title).isEmpty()) {
            Message new_Message=Message.CreateDefaultMessage(title,body);
            MessageList.add(new_Message);
        } else {
            System.out.println("이미 존재하는 제목입니다.");
        }

    }

    @Override
    public <T>String ReadMessage(T key) {
        StringBuilder list= new StringBuilder();
        for(Message now:MessageList){
            if((key instanceof String&&now.getTitle().equals(key))||
            (key instanceof UUID&&now.getId().equals(key))){
                list.append(now.toString()).append("\n");
            }
        }
        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 메세지가 없습니다.";
    }


    @Override
    public String ReadMessageAll() {
        StringBuilder list= new StringBuilder();
        for(Message now:MessageList){
            list.append(now.toString()).append("\n");

        }
        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "등록된 메세지가 없습니다.";
    }

    @Override
    public void UpdateMessageTitle(UUID ID, String change) {
        ArrayList<Message> instance = find_Message(ID);
        ChangeMessageTitle(instance,change);
    }

    @Override
    public void UpdateMessageTitle(String title, String change) {
        ArrayList<Message> instance = find_Message(title);
        ChangeMessageTitle(instance,change);
    }
    @Override
    public void UpdateMessageBody(UUID ID, String change) {
        ArrayList<Message> instance = find_Message(ID);
        ChangeMessageBody(instance,change);
    }

    @Override
    public void UpdateMessageBody(String title, String change) {
        ArrayList<Message> instance = find_Message(title);
        ChangeMessageBody(instance,change);
    }

    @Override
    public void DeleteMessage(UUID id) {
        ArrayList<Message> instance = find_Message(id);
        DeleteMessageInfo(instance);
    }

    @Override
    public void DeleteMessage(String title) {
        ArrayList<Message> instance = find_Message(title);
        DeleteMessageInfo(instance);
    }


    private void DeleteMessageInfo(ArrayList<Message> instance) {
        if (instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
        } else if (instance.size() == 1) {
            MessageList.remove(instance.get(0));
            System.out.println("성공적으로 삭제했습니다.");
        } else {
            System.out.println("중복된 메세지가 있습니다.");
        }
    }

    private ArrayList<Message> find_Message(UUID ID) {
        return new  ArrayList<>(
                MessageList.stream().filter(user -> user.getId().equals(ID)).toList()
        );
    }


    private ArrayList<Message> find_Message(String title) {
        return new  ArrayList<>(
                MessageList.stream().filter(user -> user.getTitle().equals(title)).toList()
        );
    }



    private void ChangeMessageTitle(ArrayList<Message> instance, String changetitle) {
        if (instance==null||instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
        } else if (instance.size() == 1) {
            Message find = instance.get(0);
            find.setTitle(changetitle);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
        }
    }

    private void ChangeMessageBody(ArrayList<Message> instance, String changeBody) {
        if (instance==null||instance.isEmpty())  {
            System.out.println( "해당하는 메세지가가 없습니다.");
        } else if (instance.size() == 1) {
            Message find = instance.get(0);
            find.setMessageBody(changeBody);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
        }
    }

    @Override
    public void AddMessage(Message m) {
        MessageList.add(m);
    }


}
