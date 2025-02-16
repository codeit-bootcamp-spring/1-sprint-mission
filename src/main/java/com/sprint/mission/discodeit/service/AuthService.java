package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.service.file.FileUserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final FileUserSearchService searchService;

    public User login(AuthLoginRequest request) {
        return searchService.searchUserByName(request.name());
    }
}
