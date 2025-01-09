package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class JCFUserService implements UserService{
    private final List<User> data;

    public JCFUserService(){
        this.data = new ArrayList<>();
    }

    @Override
    public void registerUser(User user){
        data.add(user);
    }

    @Override
    public void updateUserName(User user, String newName){
        data.stream()
                .findFirst()
                .ifPresentOrElse(
                        user1 -> user.setUserName(newName),
                        () -> {
                            try {
                                throw new NoSuchElementException("사용자가 없습니다");
                            } catch (NoSuchElementException e) {
                                System.out.println(e.getMessage());
                            }
                        });
    }

    @Override
    public void updateUserEmail(User user, String newEmail){
        data.stream()
                .findFirst()
                .ifPresentOrElse(
                        user1 -> user.setUserName(newEmail),
                        () -> {
                            try {
                                throw new NoSuchElementException("사용자가 없습니다");
                            } catch (NoSuchElementException e) {
                                System.out.println(e.getMessage());
                            }
                        });
    }


    @Override
    public void deleteUser(String name, String password){
        boolean removed = data.removeIf(user -> user.getUserName().equals(name) && user.getPassword().equals(password));
        if (removed) {
            System.out.println("사용자가 삭제되었습니다.");
        } else {
            System.out.println("사용자를 없거나 비밀번호가 일치하지 않습니다.");
        }
    }


    @Override
    public void getUserInfo(String name){
        data.stream()
                .filter(user -> user.getUserName().equals(name))
                .findFirst()
                .ifPresentOrElse(
                        user -> user.userInfo(),
                        ()->{
                            try{
                                throw new NoSuchElementException("사용자가 없습니다.");
                            }catch (NoSuchElementException e){
                                System.out.println(e.getMessage());
                            }
                        });

    }


}
