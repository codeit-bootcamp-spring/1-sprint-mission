package com.sprint.mission.discodit.Service.JCF;

import com.sprint.mission.discodit.Entity.User;
import com.sprint.mission.discodit.Service.UserService;

import java.util.List;

public class JcfUserService implements UserService {
    public final List<User> list;

    public JcfUserService(List<User> list) {
        this.list = list;
    }
    public void add(User user){
        list.add(user);
    }
    public void remove(int index){
        list.remove(index);
    }
    public void printList(){
        for (User user : list) {
            System.out.println("user = " + user);
        }
    }
    public void setList(int index, User user){
        list.set(index,user);
    }

}
