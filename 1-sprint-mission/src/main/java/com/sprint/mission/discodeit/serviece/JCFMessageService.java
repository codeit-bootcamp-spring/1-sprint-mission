package com.sprint.mission.discodeit.serviece;

import com.sprint.mission.discodeit.entity.Message;

import java.util.ArrayList;
import java.util.UUID;

public class JCFMessageService implements MessageService{
    final ArrayList<Message> MessageList=new ArrayList<>();

    @Override
    public void CreateMessage(String title,String body) {
        Message new_Message=new Message(title,body);
        MessageList.add(new_Message);
    }

    @Override
    public String ReadMessage(UUID ID) {
        StringBuilder list= new StringBuilder();
        boolean flag=false;
        for(Message now:MessageList){
            flag=true;
            if(now.getId().equals(ID)){
                list.append(now.DiplayMessageInfo()).append("\n");
            }
        }
        if(flag&& !list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 메세지가 없습니다.";
    }

    @Override
    public String ReadMessage(String title) {
        StringBuilder list= new StringBuilder();
        boolean flag=false;
        for(Message now:MessageList){
            flag=true;
            if(now.gettitle().equals(title)){
                list.append(now.DiplayMessageInfo()).append("\n");
            }
        }
        if(flag&& !list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 메세지가 없습니다.";
    }

    @Override
    public String ReadMessageAll() {
        StringBuilder list= new StringBuilder();
        boolean flag=false;
        for(Message now:MessageList){
            flag=true;
            list.append(now.DiplayMessageInfo()).append("\n");

        }
        if(flag&& !list.toString().isEmpty())
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
                MessageList.stream().filter(user -> user.gettitle().equals(title)).toList()
        );
    }



    private void ChangeMessageTitle(ArrayList<Message> instance, String changetitle) {
        if (instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
        } else if (instance.size() == 1) {
            Message find = instance.get(0);
            find.setTtitle(changetitle);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
        } else {
            System.out.println("중복된 메세지가 있습니다.");
        }
    }

    private void ChangeMessageBody(ArrayList<Message> instance, String changeBody) {
        if (instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
        } else if (instance.size() == 1) {
            Message find = instance.get(0);
            find.setMessageBody(changeBody);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
        } else {
            System.out.println("중복된 메세지가 있습니다.");
        }
    }

    @Override
    public void AddMessage(Message m) {
        MessageList.add(m);
    }


}
