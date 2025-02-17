package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service("JCFUserService")
public class JCFUserService implements UserService {

    protected  Map<UUID,User> userList=new LinkedHashMap<>();
    protected   final Set<UUID> existUserIdCheck = new HashSet<>();
    protected   final Set<String> existUserNameCheck = new HashSet<>();
    protected   final Set<String> existUserEmailCheck = new HashSet<>();

    @Autowired
    @Qualifier("FileBinaryContentService")
    private JCFBinaryContentService FileBinaryContentService;

    @Autowired
    @Qualifier("FileMessageService")
    private MessageService jfMessageService;

    @Autowired
    @Qualifier("FileUserStatusService")
    private UserStatusService jfUserStatusService;

    @Override
    public void createNewUser(String name,String password,String email) {
        User newUser;
        if((existUserNameCheck.contains(name)&&existUserEmailCheck.contains(email))){
            System.out.println("중복된 정보");
            return;
        }else{
            existUserEmailCheck.add(name);
            existUserNameCheck.add(email);
        }

        do {
            newUser=User.createDefaultUser(name,password,email);
        } while (existUserIdCheck.contains(newUser.getId()));
        existUserIdCheck.add(newUser.getId());
        userList.put(newUser.getId(),newUser);


    }

    @Override
    public <T> List<User> readUser(T key) {
        LinkedHashMap<UUID, User> foundUser=new LinkedHashMap<>();
        if (key instanceof String) {
            foundUser = findUser((String) key);
        } else if (key instanceof UUID) {
            foundUser = findUser((UUID) key);
        }

        if (foundUser.isEmpty()) {
            return null;
        }
        List<User> userLista = foundUser.values().stream()
                .toList();
        return userLista;
    }



    @Override
    public List<User> readUserAll() {

        return userList.values().stream()
                .toList();
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
    public boolean deleteUser(UUID id,String password) {
        LinkedHashMap<UUID,User> instance = findUser(id);
        return deleteUserInfo(instance, password);
    }


    @Override
    public boolean deleteUser(String name,String password) {
        LinkedHashMap<UUID,User> instance = findUser(name);
        return deleteUserInfo(instance, password);
    }

    @Override
    public <T> void updateUserStatus(T user) {
        List<User> userList = readUser(user);

        if (userList == null || userList.isEmpty()) {
            System.err.println("해당 사용자를 찾을 수 없습니다.");
            return;
        }

        UserStatus userStatus = userList.get(0).getUserStatus();
        if (userStatus == null) {
            System.err.println("사용자의 상태 정보를 찾을 수 없습니다.");
            return;
        }

        UUID find = userStatus.getId();
        if (find == null) {
            System.err.println("사용자의 상태 ID가 존재하지 않습니다.");
            return;
        }

        UserStatus existingStatus = jfUserStatusService.findById(find);
        if (existingStatus == null) {
            System.err.println("해당 ID의 사용자 상태 정보를 찾을 수 없습니다.");
            return;
        }

        existingStatus.updateIsOnlien();
    }

    @Override
    public boolean updateUserSelfImg(String name,char [] img){
        LinkedHashMap<UUID,User> instance = findUser(name);
        return updateSelfImg(instance,img);
    }

    @Override
    public boolean updateUserSelfImg(UUID id,char [] img){
        LinkedHashMap<UUID,User> instance = findUser(id);
        return updateSelfImg(instance,img);
    }

    @Override
    public <T, C, K> boolean sendMessageToUser(T sender, C receiver, K message) {
        List<User> senderUser=new ArrayList<>();

        if( sender instanceof String){
            senderUser=readUser((String)sender);
        }else if(sender instanceof UUID)
        {
            senderUser=readUser((UUID)sender);
        }

        List<User> reciverUser=new ArrayList<>();

        if( receiver instanceof String){
            reciverUser=readUser((String)receiver);
        }else if(receiver instanceof UUID)
        {
            reciverUser=readUser((UUID)receiver);
        }

        List<Message> messagesList = new ArrayList<>(jfMessageService.readMessage(message));
        if(reciverUser==null||senderUser==null||messagesList==null)
            return false;
        if(reciverUser.isEmpty() || senderUser.isEmpty() || messagesList.isEmpty())
            return false;
        Message messageGet=messagesList.get(0);

        User sendUser=senderUser.get(0);
        User reviceUser=reciverUser.get(0);


        messageGet.updateReceiver(reviceUser.getId(),reviceUser.getName());
        messageGet.updateSender(sendUser.getId(),sendUser.getName());

        sendUser.addMessageToUser(messageGet);
        reviceUser.addMessageToUser(messageGet);
        return true;
    }

    protected boolean updateSelfImg(LinkedHashMap<UUID,User> instance,char [] img){
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 유저가 없습니다.");
            return false;
        }else if(instance.size()==1){
            User firstUser = instance.entrySet().iterator().next().getValue();
            if(firstUser.getSelfImg()==null)
                firstUser.setSelfImg(FileBinaryContentService.create(img));
            else
                firstUser.updateBinaryContent(img);
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }
        else{
            System.out.println("중복된 유저가 있습니다.");
            return false;
        }
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

        //System.out.println(readUserAll());

        findUser.put(id,userList.get(id));
        return findUser;
    }



    protected boolean deleteUserInfo(LinkedHashMap<UUID,User> instance,String password){
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 유저가 없습니다.");
            return false;
        }else if(instance.size()==1){
            User firstUser = instance.entrySet().iterator().next().getValue();
            if(firstUser.checkPassword(password)){
                System.out.println("비번틀림");
                return false;
            }
            userList.remove(firstUser.getId());
            existUserIdCheck.remove(firstUser.getId());
            existUserNameCheck.remove(firstUser.getName());
            existUserEmailCheck.remove(firstUser.getEmail());

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
        } else if(existUserNameCheck.contains(changeName)){
            System.out.println("이미 존재하는 이름입니다");
            return false;

        }else if (instance.size() == 1) {
            User firstUser = instance.entrySet().iterator().next().getValue();
            existUserNameCheck.remove(firstUser.getName());
            firstUser.updateName(changeName);
            firstUser.updateUpdatedAt();
            existUserNameCheck.add(changeName);
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            return false;
        }

    }



}
