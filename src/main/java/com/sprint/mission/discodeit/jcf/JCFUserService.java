package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.Impl.ValidatorImpl;
import com.sprint.mission.discodeit.validation.Validator;

import java.util.*;

public class JCFUserService implements UserService {
    private final Validator validator = new ValidatorImpl();
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }


    @Override
    public User create(User user) {

        if(!validator.emailIsValid(user.getEmail())){
            System.out.println(user.getUsername() + "님의 사용자 등록이 완료되지 않았습니다.");
            System.out.println("Invalid email format: " + user.getEmail());
            return null;
        }

        if(!validator.phoneNumberIsValid(user.getPhoneNumber())){
            System.out.println(user.getUsername() + "님의 사용자 등록이 완료되지 않았습니다.");
            System.out.println("Invalid phoneNumber format(000-0000-0000) : " + user.getPhoneNumber());
            return null;
        }

        try {
            data.put(user.getId(), user);
            System.out.println(user.getUsername() + "님의 사용자 등록이 완료되었습니다.");
            return user;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @Override
    public User read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, User updatedUser) {
        if(data.containsKey(id)){
            User existingUser = data.get(id);
            if(!validator.emailIsValid(updatedUser.getEmail())){
                System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
                System.out.println("Invalid email format: " + updatedUser.getEmail());
                return null;
            }

            if(!validator.phoneNumberIsValid(updatedUser.getPhoneNumber())){
                System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
                System.out.println("Invalid phoneNumber format(000-0000-0000) : " + updatedUser.getPhoneNumber());
                return null;
            }

            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setAddr(updatedUser.getAddr());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setHobby(updatedUser.getHobby());
            existingUser.setInterest(updatedUser.getInterest());
            updatedUser.update();
            return existingUser;
        }else{
            System.out.println("저장되지 않았거나, 삭제된 아이디 입니다.");
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        try{
            String removeUserName = data.get(id).getUsername();
            data.remove(id);
            System.out.println(removeUserName +" 삭제가 완료되었습니다.");
            return true;
        } catch (NullPointerException e){
            System.out.println("유효하지 않은 ID 입니다..\n" + e);
        }
        return false;
    }


}
