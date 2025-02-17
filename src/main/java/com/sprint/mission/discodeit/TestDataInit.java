package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class TestDataInit {

    private final UserRepository userRepository;

    public void init() {

    }
}
