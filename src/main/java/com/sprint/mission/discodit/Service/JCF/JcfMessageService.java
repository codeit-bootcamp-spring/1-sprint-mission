package com.sprint.mission.discodit.Service.JCF;

import java.util.List;

public class JcfMessageService {
    public final List<Integer> list;

    public JcfMessageService(List<Integer> list) {
        this.list = list;
    }
    public void add(int i){
        list.add(i);
    }
    public void remove(int index){
        list.remove(index);
    }
    public void printList(){
        System.out.println("list = " + list);
    }
    public void setList(int index, int num){
        list.set(index,num);
    }
}
