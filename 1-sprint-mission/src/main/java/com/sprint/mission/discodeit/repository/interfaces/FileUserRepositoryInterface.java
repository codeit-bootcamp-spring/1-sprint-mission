package com.sprint.mission.discodeit.repository.file.interfacepac;

import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.util.UUID;

public interface FileUserRepositoryInterface {

    public User createUser(String nickname) throws IOException, ClassNotFoundException;

    public void modifyUser(UUID id, String newNickname) throws IOException, ClassNotFoundException;

    public boolean checkUserId(UUID id) throws IOException, ClassNotFoundException;

    public void deleteUser(UUID id) throws IOException, ClassNotFoundException;

    public User getUser(User user) throws IOException, ClassNotFoundException;
}
