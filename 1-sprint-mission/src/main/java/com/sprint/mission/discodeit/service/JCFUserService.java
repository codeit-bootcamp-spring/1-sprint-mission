package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService{

    private final List<User> userList=new ArrayList<>();

    @Override
    public void CreateUserDefault(String name) {
        if(find_user(name).isEmpty()){
            User new_user=User.createDefaultUser(name);
            userList.add(new_user);
        }
        else{
            System.out.println("이미 존재하는 이름입니다.");
        }
    }

    @Override
    public <T> String ReadUser(T key) {
        StringBuilder list = new StringBuilder();
        for (User now : userList) {
            if ((key instanceof String && now.getName().equals(key)) ||
                    (key instanceof UUID && now.getId().equals(key))) {
                list.append(now.toString()).append("\n");

            }
        }
        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 유저가 없습니다.";
    }

    @Override
    public String ReadUserAll() {
        StringBuilder list= new StringBuilder();
        for(User now:userList){
            list.append(now.toString()).append("\n");

        }
        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "등록된 유저가 없습니다.";
    }

    @Override
    public boolean UpdateUserName(String name,String changeName) {
        ArrayList<User> instance = find_user(name);
        return updateUserNameInfo(instance,changeName);
    }

    @Override
    public boolean UpdateUserName(UUID id,String changeName) {
        ArrayList<User> instance = find_user(id);
        return updateUserNameInfo(instance,changeName);
    }

    @Override
    public boolean DeleteUser(UUID id) {
        ArrayList<User> instance = find_user(id);
        return deleteUserInfo(instance);
    }


    @Override
    public boolean DeleteUser(String name) {
        ArrayList<User> instance = find_user(name);
        return deleteUserInfo(instance);
    }


    private ArrayList<User> find_user(String name){
        return new ArrayList<>(
                userList.stream().filter(user -> user.getName().equals(name)).toList()
        );
    }

    private ArrayList<User> find_user(UUID ID){
        return new  ArrayList<>(
                userList.stream().filter(user -> user.getId().equals(ID)).toList()
        );
    }

    private boolean deleteUserInfo(ArrayList<User> instance){
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 유저가 없습니다.");
            return false;
        }else if(instance.size()==1){
            userList.remove(instance.get(0));
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        }
        return false;
    }

    private boolean updateUserNameInfo(ArrayList<User> instance, String changeName) {
        if (instance==null||instance.isEmpty()) {
            System.out.println( "해당하는 유저가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            User find = instance.get(0);
            find.setName(changeName);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }
        return false;
    }


}
