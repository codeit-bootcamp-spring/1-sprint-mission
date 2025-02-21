package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUserSearchService {

    private final UserRepository userRepository;

    public User searchUserByName(String name) {

        return userRepository.findUserByName(name);
    }
}
