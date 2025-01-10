package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

        public class Channel {
            private final UUID id;
            private final User owner;
            private final List<Message> messageList;
            private final List<User> memberList;
            private final long createdAt;
            private long updatedAt;
            private String title;

            //생성자
            public Channel(String title, User owner) {
                id = UUID.randomUUID();
                createdAt = System.currentTimeMillis();
                this.title = title;
                this.owner = owner;
                messageList = new ArrayList<>();
                memberList = new ArrayList<>();
                memberList.add(owner);
            }

            //getter 메소드
            public UUID getId() {
                return id;
            }

            public long getCreatedAt() {
                return createdAt;
            }

            public long getUpdatedAt() {
                return updatedAt;
            }

            public String getTitle() {
                return title;
            }

            public User getOwner() {
                return owner;
            }

            public List<Message> getMessageList() {
                return messageList;
            }

            public List<User> getMemberList() {
                return memberList;
            }

            public void updateUpdatedAt() {
                updatedAt = System.currentTimeMillis();
            }

            public void updateTitle(String title) {
                this.title = title;
                updateUpdatedAt();
            }
            public void addMember(User newMember) {
                memberList.add(newMember);
                updateUpdatedAt();
            }

            public void removeMember(User newMember) {
                memberList.remove(newMember);
                updateUpdatedAt();
            }

            @Override
            public String toString() {
                return "Channel{title:" + title + ",owner:" + owner.getName()
                        + ",createdAt:" + createdAt + ",updatedAt:" + updatedAt + "}";
            }
}