package com.sprint.mission.discodeit.entity;




import com.sprint.mission.discodeit.exception.PassWordFormatException;


import java.io.Serializable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String passWord;
    private String email;
    private String alias;
    private List<UUID> attending;



    public static class Builder {
        private final UUID id;
        private final Long createdAt;
        private Long updatedAt;
        private String passWord;
        private String email;
        private String alias;

        private List<UUID> attending; //다대일


        public Builder() {
            this.id = UUID.randomUUID();
            this.createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            this.attending = new ArrayList<>();
        }

        public Builder buildUpdatedAt() {
            this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            return this;
        }

        public Builder buildEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder buildPassWord(String passWord) throws PassWordFormatException{
            if (passWord.length() < 12) {
                throw new PassWordFormatException("비밀번호는 12자리 이상이어야 합니다.");

            }
            this.passWord = passWord;
            return this;
        }

        public Builder buildAlias(String alias) {
            this.alias = alias;
            return this;
        }
//        public Builder buildAttending() {
//            this.attending = null;
//            return this;
//        }

        public User build() {
            return new User(this);
        }
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.email = builder.email;
        this.passWord = builder.passWord;
        this.alias = builder.alias;
        this.attending = builder.attending;
    }









//    Use Builder Pattern hence complicated constructor
//    public User(String email, String alias, String passWord) {
//        id = UUID.randomUUID();
//        createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//        updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//        this.email = email;
//        this.alias = alias;
//        this.passWord = passWord;
//    }


    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getPassWord() {
        return passWord;
    }
    public String getEmail() {
        return email;
    }
    public String getAlias() {
        return alias;
    }
    public List<UUID> getAttending() {
        return attending;
    }


    public void setPassWord(String pw) {
        passWord = pw;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
    public void setAttending(UUID channel) {
        this.attending.add(channel);
    }
    @Override
    public String toString() {
        return "User ID : " + getId().toString().substring(0, 5) + ", Alias : " + getAlias() + ", created at : " + getCreatedAt() + ", Updated at : " + getUpdatedAt() + ", e-mail : " + getEmail() + ", Attending : " + getAttending();
    }


}
