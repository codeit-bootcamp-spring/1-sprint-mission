package com.sprint.mission.discodit.Service.JCF;

import com.sprint.mission.discodit.Service.UserService;

import java.util.List;

public class JcfUserService implements UserService {
    public final List<Integer> list;

    public JcfUserService(List<Integer> list) {
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
