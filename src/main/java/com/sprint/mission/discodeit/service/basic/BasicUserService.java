package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DataNotFoundException;
import com.sprint.mission.discodeit.exception.ValidationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.Impl.ValidatorImpl;
import com.sprint.mission.discodeit.validation.Validator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final Validator validator = new ValidatorImpl();
    private final UserRepository repository;

    public BasicUserService(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public User create(User user) {

        if(!validator.isValidEmail(user.getEmail())){
            System.out.println(user.getUsername() + "님의 사용자 등록이 완료되지 않았습니다.");
            System.out.println(new ValidationException("Invalid email format : " + user.getEmail()));
        }

        if(!validator.isValidPhoneNumber(user.getPhoneNumber())){
            System.out.println(user.getUsername() + "님의 사용자 등록이 완료되지 않았습니다.");
            System.out.println(new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + user.getPhoneNumber()));
        }

        repository.save(user);
        return  user;
    }

    @Override
    public User readOne(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<User> readAll() {
        return repository.readAll();
    }

    @Override
    public User update(UUID id, User updatedUser) {
        try {
            if(!validator.isValidEmail(updatedUser.getEmail())){
                System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
                System.out.println(new ValidationException("Invalid email format : " + updatedUser.getEmail()));
                return null;
            }

            if(!validator.isValidPhoneNumber(updatedUser.getPhoneNumber())){
                System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
                System.out.println(new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + updatedUser.getPhoneNumber()));
                return null;
            }

            return repository.modify(id, updatedUser);
        } catch (DataNotFoundException e){
            throw new DataNotFoundException("저장되지 않았거나, 삭제된 아이디 입니다." + id);
        }
    }

    @Override
    public boolean delete(UUID id) {
        return repository.deleteById(id);
    }
}
