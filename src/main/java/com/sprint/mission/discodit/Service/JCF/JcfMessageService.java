package com.sprint.mission.discodit.Service.JCF;

import com.sprint.mission.discodit.Entity.Message;

import java.util.List;

public class JcfMessageService {
    public final List<Message> list;

    public JcfMessageService(List<Message> list) {
        this.list = list;
    }
    public void add(Message i){
        list.add(i);
    }
    public void remove(int index){
        list.remove(index);
    }
    public void printList(){
        for (Message message : list) {
            System.out.println("message = " + message);
        }
    }
    public void setList(int index, Message message){
        list.set(index,message);
    }
}
