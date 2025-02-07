package com.sprint.mission.service.jcf.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JCFUserService userService;
}
