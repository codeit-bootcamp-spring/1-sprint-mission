package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    protected  Map<UUID,User> userList=new LinkedHashMap<>();
    protected   final Set<UUID> existUserIdCheck = new HashSet<>();

    @Override
    public void createNewUser(String name) {
        User newUser;
        do {
            newUser=User.createDefaultUser(name);
        } while (existUserIdCheck.contains(newUser.getId()));
        existUserIdCheck.add(newUser.getId());
        userList.put(newUser.getId(),newUser);


    }

    @Override
    public <T> String readUser(T key) {
        StringBuilder list=new StringBuilder();
        LinkedHashMap<UUID, User> foundUser=new LinkedHashMap<>();

        if (key instanceof String) {
            foundUser = findUser((String) key);
        } else if (key instanceof UUID) {
            foundUser = findUser((UUID) key);
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
    public String readUserAll() {
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
    public boolean updateUserName(String name,String changeName) {
        LinkedHashMap<UUID,User> instance = findUser(name);
        return updateUserNameInfo(instance,changeName);
    }

    @Override
    public boolean updateUserName(UUID id,String changeName) {
        LinkedHashMap<UUID,User> instance = findUser(id);
        return updateUserNameInfo(instance,changeName);
    }

    @Override
    public boolean deleteUser(UUID id) {
        LinkedHashMap<UUID,User> instance = findUser(id);
        return deleteUserInfo(instance);
    }


    @Override
    public boolean deleteUser(String name) {
        LinkedHashMap<UUID,User> instance = findUser(name);
        return deleteUserInfo(instance);
    }


    protected LinkedHashMap<UUID,User> findUser(String name){
        LinkedHashMap<UUID,User> findUser=new LinkedHashMap<>();
        for (User user : userList.values()) {
            if (user.getName().equals(name)) {
                findUser.put(user.getId(),user);
            }
        }
        return findUser;
    }

    protected LinkedHashMap<UUID,User> findUser(UUID id){
        LinkedHashMap<UUID,User> findUser=new LinkedHashMap<>();
        findUser.put(id,userList.get(id));
        return findUser;
    }

    protected boolean deleteUserInfo(LinkedHashMap<UUID,User> instance){
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 유저가 없습니다.");
            return false;
        }else if(instance.size()==1){
            User firstUser = instance.entrySet().iterator().next().getValue();
            userList.remove(firstUser.getId());
            existUserIdCheck.remove(firstUser.getId());
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        }
        else{
            System.out.println("중복된 유저가 있습니다.");
            return false;
        }
    }

    protected boolean updateUserNameInfo(LinkedHashMap<UUID,User> instance, String changeName) {
        if (instance==null||instance.isEmpty()) {
            System.out.println( "해당하는 유저가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            User firstUser = instance.entrySet().iterator().next().getValue();
            firstUser.updateName(changeName);
            firstUser.updateUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            return false;
        }

    }


}
