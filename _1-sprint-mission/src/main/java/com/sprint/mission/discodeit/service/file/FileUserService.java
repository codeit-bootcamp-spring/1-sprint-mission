//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.util.*;
//
//public class FileUserService implements UserService {
//    private final FileUserRepository fileuserRepository;
//
//    public FileUserService(FileUserRepository fileuserRepository) {
//        this.fileuserRepository = fileuserRepository;
//    }
//
//    public User create(String username,String email,String password,String phoneNumber,String address){
//        User user = new User(username,email,password,phoneNumber,address);
//        return fileuserRepository.create(user, userStatus);
//    }
//    public User findById(UUID id){
//        return fileuserRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("User with id "+id+" not found"));
//    }
//    public List<User> findAll(){
//        return fileuserRepository.findAll();
//    }
//
//    @Override
//    public User update(UUID userId, String field, Object value) {
//        Optional<User> userOptional = fileuserRepository.findById(userId);
//        if(userOptional.isPresent()){
//            User user = userOptional.get();
//            switch (field) {
//                case "username":
//                    user.setUsername((String) value);
//                    break;
//                case "email":
//                    user.setEmail((String) value);
//                    break;
//                case "password":
//                    user.setPassword((String) value);
//                    break;
//                case "phoneNumber":
//                    user.setPhoneNumber((String) value);
//                    break;
//                case "address":
//                    user.setAddress((String) value);
//                    break;
//                default:
//                    throw new IllegalArgumentException("Invalid field: " + field);
//            }
//            return fileuserRepository.create(user, userStatus);
//        }
//        return null;
//    }
//
//    @Override
//    public void delete(UUID userId) {
//        fileuserRepository.delete(userId);
//    }
//}
