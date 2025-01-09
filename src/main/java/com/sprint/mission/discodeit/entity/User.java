package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String username;  // 사용자 이름
    private String email;     // 사용자 이메일

    public User(String username, String email) {
        super();  // BaseEntity의 생성자 호출
        this.username = username;
        this.email = email;
    }

    // Getter 메서드
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // Setter (Update) 메서드
    public void updateUsername(String username) {
        this.username = username;
        setUpdateAT(System.currentTimeMillis()); // 수정 시간 업데이트
    }

    public void updateEmail(String email) {
        this.email = email;
        setUpdateAT(System.currentTimeMillis()); // 수정 시간 업데이트
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdateAT() +
                '}';
    }

}
