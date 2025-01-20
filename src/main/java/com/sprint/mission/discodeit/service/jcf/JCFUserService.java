package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.common.validation.Validator;
import com.sprint.mission.discodeit.common.validation.ValidatorImpl;
import com.sprint.mission.discodeit.dto.UserReqDTO;
import com.sprint.mission.discodeit.dto.UserResDTO;
import com.sprint.mission.discodeit.dto.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.RegionCode;
import com.sprint.mission.discodeit.enums.UserType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.UserService;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// TODO: Exception Throw 하는 메소드 있는 곳은 try-catch 우선 적용해두기 (예외처리는 추후에)
public class JCFUserService implements UserService {
    // DB 대체로 생각함
    private final Validator validator = new ValidatorImpl();
    private final Map<Long, User> userData;
    private final AtomicLong idGenerator;

    public JCFUserService() {
        this.userData = new HashMap<>();    // 데이터 저장소
        this.idGenerator = new AtomicLong(1);   // ID 초기값 1
    }

    @Override
    public Long createUserData(String username, String nickname, String email, String password, String regionCode, String phone, String imgPath) {
        // 유저 생성 로직
        try {
            if (!validator.userNameValidator(username)) {
                throw new CustomException(ErrorCode.INVALID_USERNAME);
            }

            if (!validator.nicknameValidator(nickname)) {
                throw new CustomException(ErrorCode.INVALID_NICKNAME);
            }

            if (!validator.passwordValidator(password)) {
                throw new CustomException(ErrorCode.INVALID_PASSWORD);
            }

            if (!validator.phoneNumValidator(phone)) {
                throw new CustomException(ErrorCode.INVALID_PHONENUM);
            }

            if (!validator.emailValidator(email)) {
                throw new CustomException(ErrorCode.INVALID_EMAIL);
            }

            if (StringUtils.isBlank(regionCode)) {
                throw new CustomException(ErrorCode.REGION_CODE_IS_NOT_NULL);
            }

            imgPath = StringUtils.isBlank(imgPath) ? "defaultImg.png" : imgPath;

            User user = new User(new UserReqDTO(username, nickname, email, password, regionCode, phone, imgPath));  // 유저 생성
            Long id = idGenerator.getAndIncrement();    // 1++
            userData.put(id, user);
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: Custom Exception으로 바꾸기~
    @Override
    public UserResDTO getUser(Long id) {
        User user = Objects.requireNonNull(findUserById(id), "해당 ID의 사용자가 존재하지 않습니다.");
        return new UserResDTO(id, user);
    }

    @Override
    public User getUserToUserObj(Long id) {
        return Objects.requireNonNull(findUserById(id), "해당 ID의 사용자가 존재하지 않습니다.");
    }

    @Override
    public UserResDTO getUser(String userName) {
        Optional<Map.Entry<Long, User>> user = Objects.requireNonNull(findUserByUserName(userName), "해당 이름의 사용자가 존재하지 않습니다.");
        return new UserResDTO(user.get().getKey(), user.get().getValue());
    }

    @Override
    public List<UserResDTO> getAllUser() {
        return userData.entrySet().stream()
                .map(entry ->
                        new UserResDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public User findUserById(Long id) {
        return userData.get(id);
    }

    // userName으로 User 객체와 해당 Long ID 반환
    public Optional<Map.Entry<Long, User>> findUserByUserName(String userName) {
        return userData.entrySet().stream()
                .filter(entry -> entry.getValue().getUserName().getName().equals(userName))
                .findFirst();
    }

    @Override
    public boolean updateUser(Long id, UserUpdateDTO updateInfo) {
        boolean isUpdated = false;
        User user = getUserToUserObj(id);
        try {
            if (updateInfo.getUserName() != null && !user.getUserName().getName().equals(updateInfo.getUserName()) && validator.userNameValidator(updateInfo.getUserName())) {
                user.updateUserName(updateInfo.getUserName());
                isUpdated = true;
            }

            if (updateInfo.getNickname() != null && !user.getNickname().getName().equals(updateInfo.getNickname()) && validator.nicknameValidator(updateInfo.getNickname())) {
                user.updateNickname(updateInfo.getNickname());
                isUpdated = true;
            }

            if (updateInfo.getEmail() != null && !user.getEmail().getEmail().equals(updateInfo.getEmail()) && validator.emailValidator(updateInfo.getEmail())) {
                user.updateEmail(updateInfo.getEmail());
                isUpdated = true;
            }

            if (updateInfo.getUserType() != null && (user.getUserType() != UserType.fromString(updateInfo.getUserType().toUpperCase()))) {
                user.updateUserType(UserType.fromString(updateInfo.getUserType().toUpperCase()));
                isUpdated = true;
            }

            // phone
            if ((updateInfo.getRegionCode() != null && updateInfo.getPhone() != null)
                    && (!user.getPhone().getPhoneNum().equals(updateInfo.getPhone()) || user.getPhone().getRegionCode() != RegionCode.fromString(updateInfo.getRegionCode().toUpperCase()))
                    && validator.phoneNumValidator(updateInfo.getPhone())) {
                user.updatePhone(updateInfo.getPhone(), updateInfo.getRegionCode().toUpperCase());
                isUpdated = true;
            }

            if (updateInfo.getImgPath() != null && !user.getUserImgPath().equals(updateInfo.getImgPath())) {
                user.updateUserImg(updateInfo.getImgPath());
                isUpdated = true;
            }

            if (updateInfo.getIntroduce() != null && !user.getIntroduce().equals(updateInfo.getIntroduce())) {
                user.updateIntroduce(updateInfo.getIntroduce());
                isUpdated = true;
            }
            userData.put(id, user); // DB에 반영
            return isUpdated;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResDTO deleteUser(Long id) {
        UserResDTO deleteUser = getUser(id);
        userData.remove(deleteUser.getId());
        return deleteUser;
    }

    @Override
    public UserResDTO deleteUser(String userName) {
        UserResDTO deleteUser = getUser(userName);
        userData.remove(deleteUser.getId());
        return deleteUser;
    }
}