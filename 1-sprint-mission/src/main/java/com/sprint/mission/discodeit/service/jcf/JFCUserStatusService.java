package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("JFCUserStatusService")
public class JFCUserStatusService implements UserStatusService {
    protected Map<UUID, UserStatus> userStatusMap = new HashMap<>();

    protected final HashSet<UUID> userRepository;

    public JFCUserStatusService() {
        this.userRepository = new HashSet<>();
    }

    @Override
    public boolean create(UUID id) {

        if (!userRepository.contains(id)) {

            return false;
        }
        boolean duplicateExists = userStatusMap.values().stream()
                .anyMatch(us -> us.getUserId().equals(id));
        if (duplicateExists) {

            return false;
        }

        UserStatus newUserStatus = UserStatus.setUpUserStatus(id);
        userStatusMap.put(newUserStatus.getId(), newUserStatus);
        return true;
    }



    @Override
    public UserStatus findById(UUID id) {
        return userStatusMap.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public boolean update(UUID id) {
        UserStatus updatedUserStatus = UserStatus.setUpUserStatus(id);
        userStatusMap.put(id, updatedUserStatus);

        return true;
    }

    @Override
    public boolean updateByUserId(UUID id) {
        UserStatus existing = userStatusMap.values().stream()
                .filter(us -> us.getUserId().equals(id))
                .findFirst()
                .orElse(null);
        if (existing == null) {
            return false;
        }
        UserStatus updatedUserStatus = UserStatus.setUpUserStatus(id);
        userStatusMap.put(existing.getId(), updatedUserStatus);
        return true;
    }


    @Override
    public void delete(List<UserStatus> list){
        for(UserStatus i:list){
            userStatusMap.remove(i.getId());
        }
    }

    @Override
    public List<UUID> getOnlineUsers() {
        List<UUID> onlineUsers = new ArrayList<>();

        for (UserStatus status : userStatusMap.values()) {
            if (status.isUserOnline()) {
                UUID user = status.getUserId();
                if (user != null) {
                    onlineUsers.add(user);
                }
            }
        }

        return onlineUsers;
    }

    public void add(UserStatus userStatus){
        userStatusMap.put(userStatus.getId(),userStatus);
    }



}
