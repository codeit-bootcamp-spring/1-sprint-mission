package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable {

    private static final Long serialVersionUID = 1L;
    private UUID id ;
    private final Instant createdAt;
    private Instant updatedAt;

    private String userName;
    private String password;
    private String email;

    //고유한 UserStatus, BinaryContent 생성
    private UserStatus userStatus;
    private BinaryContent binaryContent;

    public User(UserCreateDTO userCreateDTO){
        this.id = UUID.randomUUID();
        this.createdAt =  Instant.now();
        this.userName = userCreateDTO.name();
        this.password=userCreateDTO.password();
        this.email=userCreateDTO.email();
        this.userStatus = new UserStatus(this.id);
        updateBinaryContent(userCreateDTO.filePath());
    }

    //update
    public void updateUpdatedAt(){
        this.updatedAt=Instant.now(); //업데이트 시간
    }

    public void updateUser(UserUpdateDTO userUpdateDTO) {
        updateUserName(userUpdateDTO.newName());
        updatePassword(userUpdateDTO.newPassword());
        updateEmail(userUpdateDTO.newEmail());
        updateBinaryContent(userUpdateDTO.newFilePAth());
        updateUpdatedAt();
    }

    private void updateUserName(String newName) {
        this.userName = Objects.requireNonNullElse(newName, this.userName);
    }

    private void updatePassword(String newPassword) {
        this.password = Objects.requireNonNullElse(newPassword, this.password);
    }

    private void updateEmail(String newEmail) {
        this.email = Objects.requireNonNullElse(newEmail, this.email);
    }

    //새로운 이미지가 들어오면, 완전히 새로운 이미지 객체로 간주 ?
    private void updateBinaryContent(String newFilePath) {
        if (newFilePath != null) {
            this.binaryContent = new BinaryContent(this.id, newFilePath.toString());
        }
    }


    //delete

    public void deleteUserStatus(){
        this.userStatus=null;
    }
    public void deleteBinaryContent(){
        this.binaryContent=null;
    }




}
