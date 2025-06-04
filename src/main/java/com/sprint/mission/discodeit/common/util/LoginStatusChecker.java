package com.sprint.mission.discodeit.common.util;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginStatusChecker {

    private final JwtService jwtService;
    private final JwtSessionRepository jwtSessionRepository;

    public boolean getOnline(User user) {
        JwtSession jwtSession = jwtSessionRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("jwt session이 존재하지 않습니다."));

        String accessToken = jwtSession.getAccessToken();
        return jwtService.validate(accessToken);
    }

}
