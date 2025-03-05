package com.sprint.mission.discodeit.service.jcf.interfaces;

import com.sprint.mission.discodeit.entity.User;

public interface JCFUserServiceInterface {
    public void showInfoUser(User user);
    public User creatUser(String nickName,String password,String email);
    public void deleteUser(User user);
    public void modifyUser(User user, String newName);

}
