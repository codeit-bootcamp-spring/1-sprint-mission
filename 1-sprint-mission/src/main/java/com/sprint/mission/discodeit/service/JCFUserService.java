package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public class JCFUserService implements UserService{

    private final Map<UUID,User> userList=new TreeMap<>();

    @Override
    public void CreateUserDefault(String name) {
        if(find_user(name).isEmpty()){
            User new_user=User.createDefaultUser(name);
            userList.put(new_user.getId(),new_user);
        }
        else{
            System.out.println("이미 존재하는 이름입니다.");
        }
    }

    @Override
    public <T> String ReadUser(T key) {
        StringBuilder list=new StringBuilder();
        TreeMap<UUID, User> foundUser=new TreeMap<>();

        if (key instanceof String) {
            foundUser = find_user((String) key);
        } else if (key instanceof UUID) {
            foundUser = find_user((UUID) key);
        }

        for (User val : foundUser.values()) {
            list.append(val.toString());
        }

        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "해당하는 유저가 없습니다.";
    }



    @Override
    public String ReadUserAll() {
        StringBuilder list= new StringBuilder();
        for (User val : userList.values()) {
            list.append(val.toString());
        }
        if(!list.toString().isEmpty())
            return list.toString();
        else
            return "등록된 유저가 없습니다.";
    }

    @Override
    public boolean UpdateUserName(String name,String changeName) {
        TreeMap<UUID,User> instance = find_user(name);
        return updateUserNameInfo(instance,changeName);
    }

    @Override
    public boolean UpdateUserName(UUID id,String changeName) {
        TreeMap<UUID,User> instance = find_user(id);
        return updateUserNameInfo(instance,changeName);
    }

    @Override
    public boolean DeleteUser(UUID id) {
        TreeMap<UUID,User> instance = find_user(id);
        return deleteUserInfo(instance);
    }


    @Override
    public boolean DeleteUser(String name) {
        TreeMap<UUID,User> instance = find_user(name);
        return deleteUserInfo(instance);
    }


    private TreeMap<UUID,User> find_user(String name){
        TreeMap<UUID,User> findUser=new TreeMap<>();
        for (User user : userList.values()) {
            if (user.getName().equals(name)) {
                findUser.put(user.getId(),user);
            }
        }
        return findUser;
    }

    private TreeMap<UUID,User> find_user(UUID id){
        TreeMap<UUID,User> findUser=new TreeMap<>();
        findUser.put(id,userList.get(id));
        return findUser;
    }

    private boolean deleteUserInfo(TreeMap<UUID,User> instance){
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 유저가 없습니다.");
            return false;
        }else if(instance.size()==1){
            User find = instance.firstEntry().getValue();
            find.deleteExistUserId();
            userList.remove(find.getId());
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        }
        else{
            System.out.println("중복된 유저가 있습니다.");
            return false;
        }
    }

    private boolean updateUserNameInfo(TreeMap<UUID,User> instance, String changeName) {
        if (instance==null||instance.isEmpty()) {
            System.out.println( "해당하는 유저가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            User find = instance.firstEntry().getValue();
            find.setName(changeName);
            find.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            return false;
        }

    }


}
