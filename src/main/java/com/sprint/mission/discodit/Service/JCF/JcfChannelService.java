package com.sprint.mission.discodit.Service.JCF;

import com.sprint.mission.discodit.Entity.Channel;

import java.util.List;

public class JcfChannelService {
    public final List<Channel> list;
    public JcfMessageService jcfMessageService;

    public void setJcfMessageService(JcfMessageService jcfMessageService) {
        this.jcfMessageService = jcfMessageService;
    }
    public void printJcfMessageService(){
        jcfMessageService.printList();
    }

    public JcfChannelService(List<Channel> list) {
        this.list = list;
    }
    public void add(Channel i){
        list.add(i);
    }
    public void remove(int index){
        list.remove(index);
    }
    public void printList(){
        for (Channel channel : list) {
            System.out.println("channel = " + channel);
        }
    }
    public void setList(int index, Channel num){
        list.set(index,num);
    }
}
