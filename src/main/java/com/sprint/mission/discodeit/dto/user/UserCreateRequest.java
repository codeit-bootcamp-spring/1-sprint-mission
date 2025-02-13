package com.sprint.mission.discodeit.dto.user;

public record UserCreateRequest (
        String username,
        String email,
        String password
){
    public UserCreateRequest(String username, String email, String password){
        validate(username, email, password);
        this.username = username;
        this.email = email;
        this.password = password;
    }
    private void validate(String username, String email, String password){
        if(username == null || username.trim().isEmpty()){
            throw new IllegalArgumentException("username 은 공백일 수 없습니다.");
        }
        if(email == null || !email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new IllegalArgumentException("email 이 잘못되었습니다.");
        }
        if(password == null || password.trim().isEmpty()){
            throw new IllegalArgumentException("password 가 잘못되었습니다.");
        }
    }

}
