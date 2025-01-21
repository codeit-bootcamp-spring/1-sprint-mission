package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DataNotFoundException;
import com.sprint.mission.discodeit.exception.IdNotFoundException;
import com.sprint.mission.discodeit.exception.ValidationException;
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
            System.out.println(new ValidationException("Invalid email format : " + user.getEmail()));
        }

        if(!validator.phoneNumberIsValid(user.getPhoneNumber())){
            System.out.println(user.getUsername() + "님의 사용자 등록이 완료되지 않았습니다.");
            System.out.println(new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + user.getPhoneNumber()));
        }

        try {
            data.put(user.getId(), user);
            System.out.println(user.getUsername() + "님의 사용자 등록이 완료되었습니다.");
            return user;
        }catch (Exception e){
            throw new RuntimeException("User creation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public User read(UUID id) {
        try{
            Optional<User> searchUser = Optional.ofNullable(data.get(id));

            if(searchUser.isPresent()) {
                User user = searchUser.get();

                System.out.println("userName : " + user.getUsername()
                        + " | Email : " + user.getEmail()
                        + " | phoneNumber : " + user.getPhoneNumber()
                        + " | Address : " + user.getAddr()
                        + " | Age : " + user.getAge()
                        + " | Hobby : " + user.getHobby()
                        + " | Interest : " + user.getInterest()
                );
                return user;
            }else{
                throw new DataNotFoundException("사용자가 존재하지 않습니다.");
            }
        } catch (DataNotFoundException e){
            System.out.println("예외 처리 메시지 : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<User> readAll() {
        data.values().forEach(user -> {
            System.out.println("username : " + user.getUsername()
                    + " | Email : " + user.getEmail()
                    + " | phoneNumber : " + user.getPhoneNumber()
                    + " | Address : " + user.getAddr()
                    + " | Age : " + user.getAge()
                    + " | Hobby : " + user.getHobby()
                    + " | Interest : " + user.getInterest()
            );
        });
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, User updatedUser) {
        try {
            if(data.containsKey(id)){
                User existingUser = data.get(id);
                if(!validator.emailIsValid(updatedUser.getEmail())){
                    System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
                    System.out.println(new ValidationException("Invalid email format : " + updatedUser.getEmail()));
                }

                if(!validator.phoneNumberIsValid(updatedUser.getPhoneNumber())){
                    System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
                    System.out.println(new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + updatedUser.getPhoneNumber()));
                }

                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
                existingUser.setAddr(updatedUser.getAddr());
                existingUser.setAge(updatedUser.getAge());
                existingUser.setHobby(updatedUser.getHobby());
                existingUser.setInterest(updatedUser.getInterest());
                updatedUser.update();

                read(existingUser.getId());
                return existingUser;
            }else{
                System.out.println(new IdNotFoundException("아이디가 존재하지 않습니다."));
            }
        } catch (DataNotFoundException e){
            throw new DataNotFoundException("저장되지 않았거나, 삭제된 아이디 입니다." + id);
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
