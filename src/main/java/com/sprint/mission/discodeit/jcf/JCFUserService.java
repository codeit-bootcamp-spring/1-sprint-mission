//package com.sprint.mission.discodeit.jcf;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.exception.DataNotFoundException;
//import com.sprint.mission.discodeit.exception.ValidationException;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.validation.Impl.ValidatorImpl;
//import com.sprint.mission.discodeit.validation.Validator;
//
//import java.util.*;
//
//public class JCFUserService implements UserService {
//    private final Validator validator = new ValidatorImpl();
//    private final UserRepository repository;
//
//    public JCFUserService(UserRepository repository) {
//        this.repository = repository;
//    }
//
//
//    @Override
//    public User create(User user) {
//
//        if(!validator.isValidEmail(user.getEmail())){
//            System.out.println(user.getUsername() + "님의 사용자 등록이 완료되지 않았습니다.");
//            System.out.println(new ValidationException("Invalid email format : " + user.getEmail()));
//        }
//
//        if(!validator.isValidPhoneNumber(user.getPhoneNumber())){
//            System.out.println(user.getUsername() + "님의 사용자 등록이 완료되지 않았습니다.");
//            System.out.println(new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + user.getPhoneNumber()));
//        }
//
//        repository.save(user);
//        return  user;
//    }
//
//    @Override
//    public User read(UUID id) {
//        try{
//            Optional<User> searchUser = Optional.ofNullable(repository.findById(id));
//
//            if(searchUser.isPresent()) {
//                User user = searchUser.get();
//
//                System.out.println("userName : " + user.getUsername()
//                        + " | Email : " + user.getEmail()
//                        + " | phoneNumber : " + user.getPhoneNumber()
//                        + " | Address : " + user.getAddr()
//                        + " | Age : " + user.getAge()
//                        + " | Hobby : " + user.getHobby()
//                        + " | Interest : " + user.getInterest()
//                );
//                return user;
//            }else{
//                throw new DataNotFoundException("사용자가 존재하지 않습니다.");
//            }
//        } catch (DataNotFoundException e){
//            System.out.println("예외 처리 메시지 : " + e.getMessage());
//            return null;
//        }
//    }
//
//    @Override
//    public List<User> readAll() {
//        return null;
//    }
//
//    @Override
//    public void searchByUser(){
//        List<User> list = repository.findAll();
//
//        list.forEach(data -> {
//            System.out.println("username : " + data.getUsername()
//                    + " | Email : " + data.getEmail()
//                    + " | phoneNumber : " + data.getPhoneNumber()
//                    + " | Address : " + data.getAddr()
//                    + " | Age : " + data.getAge()
//                    + " | Hobby : " + data.getHobby()
//                    + " | Interest : " + data.getInterest()
//            );
//        });
//    }
//
//    @Override
//    public void searchByUserId(UUID id){
//        try {
//            User user = repository.findById(id);
//
//            System.out.println("userName : " + user.getUsername()
//                    + " | Email : " + user.getEmail()
//                    + " | phoneNumber : " + user.getPhoneNumber()
//                    + " | Address : " + user.getAddr()
//                    + " | Age : " + user.getAge()
//                    + " | Hobby : " + user.getHobby()
//                    + " | Interest : " + user.getInterest()
//            );
//        } catch (NullPointerException e){
//            System.out.println("유효하지 않은 ID 입니다..\n" + e);
//        }
//    }
//
//    @Override
//    public User update(UUID id, User updatedUser) {
//        try {
//            if(!validator.isValidEmail(updatedUser.getEmail())){
//                System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
//                System.out.println(new ValidationException("Invalid email format : " + updatedUser.getEmail()));
//                return null;
//            }
//
//            if(!validator.isValidPhoneNumber(updatedUser.getPhoneNumber())){
//                System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
//                System.out.println(new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + updatedUser.getPhoneNumber()));
//                return null;
//            }
//
//            repository.modify(id, updatedUser);
//
//            return read(id);
//        } catch (DataNotFoundException e){
//            throw new DataNotFoundException("저장되지 않았거나, 삭제된 아이디 입니다." + id);
//        }
//    }
//
//    @Override
//    public boolean delete(UUID id) {
//        return repository.deleteById(id);
//    }


//}
