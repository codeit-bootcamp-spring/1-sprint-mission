package com.sprint.mission.discodeit.service.interfaces;

import com.sprint.mission.discodeit.dto.UserDto;

import java.io.IOException;

public interface UserService {
    public void showInfoUser(UserDto user) throws IOException, ClassNotFoundException;
    public void creatUser(UserDto userDto) throws IOException, ClassNotFoundException;
    public void deleteUser(UserDto user) throws IOException, ClassNotFoundException;
    public void modifyUser(UserDto user) throws IOException, ClassNotFoundException;
}
