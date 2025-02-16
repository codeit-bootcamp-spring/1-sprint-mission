package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginUserRequest;
import com.sprint.mission.discodeit.dto.user.LoginUserResponse;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentType;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageSource messageSource;
    private final UserMapper userMapper;

    @Override
    public LoginUserResponse login(LoginUserRequest request) {
        String name = request.getName();
        String password = request.getPassword();
        //유저 존재 여부
        User user = userRepository.findByName(name)
                .orElseThrow(()-> new IllegalArgumentException(messageSource.getMessage("error.user.notfound", new Object[]{name}, LocaleContextHolder.getLocale())));

        //비밀번호 검증
        if(!user.getPassword().equals(request.getPassword())){
            throw new IllegalArgumentException(messageSource.getMessage("error.password.invalid", null, LocaleContextHolder.getLocale()));
        }

        //프로필 이미지 조회
        BinaryContent profileImage = binaryContentRepository.findByUserIdAndType(user.getId(), BinaryContentType.PROFILE).orElse(null);

        return userMapper.toLoginUserResponse(user, profileImage);

    }
}
