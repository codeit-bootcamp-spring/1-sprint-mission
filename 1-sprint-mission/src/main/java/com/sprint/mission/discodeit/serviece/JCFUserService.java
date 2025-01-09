package com.sprint.mission.discodeit.serviece;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.UUID;

public class JCFUserService implements UserService{

    final ArrayList<User> userList=new ArrayList<>();

    @Override
    public void CreateUser(String name) {
        User new_user=new User(name);
        userList.add(new_user);
    }

    @Override
    public String ReadUser(String name) {
        StringBuilder list= new StringBuilder();
        boolean flag=false;
        for(User now:userList){
            flag=true;
            if(now.getName().equals(name)){
                list.append(now.DisplayInfo()).append("\n");
            }
        }
        if(flag&& !list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 유저가 없습니다.";
    }

    @Override
    public String ReadUser(UUID ID) {
        StringBuilder list= new StringBuilder();
        boolean flag=false;
        for(User now:userList){
            flag=true;
            if(now.getId().equals(ID)){
                list.append(now.DisplayInfo()).append("\n");
            }
        }
        if(flag&& !list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 유저가 없습니다.";
    }

    @Override
    public String ReadUserAll() {
        StringBuilder list= new StringBuilder();
        boolean flag=false;
        for(User now:userList){
            flag=true;
            list.append(now.DisplayInfo()).append("\n");

        }
        if(flag&& !list.toString().isEmpty())
            return list.toString();
        else
            return "등록된 유저가 없습니다.";
    }

    @Override
    public void UpdateUserName(String name,String changeName) {
        ArrayList<User> instance = find_user(name);
        updateUserNameInfo(instance,changeName);
    }

    @Override
    public void UpdateUserName(UUID id,String changeName) {
        ArrayList<User> instance = find_user(id);
        updateUserNameInfo(instance,changeName);
    }

    @Override
    public void DeleteUser(UUID id) {
        ArrayList<User> instance = find_user(id);
        deleteUserInfo(instance);
    }


    @Override
    public void DeleteUser(String name) {
        ArrayList<User> instance = find_user(name);
        deleteUserInfo(instance);
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

    private void deleteUserInfo(ArrayList<User> instance){
        if(instance.isEmpty()){
            System.out.println("해당하는 유저가 없습니다.");
        }else if(instance.size()==1){
            userList.remove(instance.get(0));
            System.out.println("성공적으로 삭제했습니다.");
        }else{
            System.out.println("중복된 유저가 있습니다.");
        }

    }

    private void updateUserNameInfo(ArrayList<User> instance, String changeName) {
        if (instance.isEmpty()) {
            System.out.println( "해당하는 유저가 없습니다.");
        } else if (instance.size() == 1) {
            User find = instance.get(0);
            find.setName(changeName);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
        } else {
            System.out.println("중복된 유저가 있습니다.");
        }
    }


}
