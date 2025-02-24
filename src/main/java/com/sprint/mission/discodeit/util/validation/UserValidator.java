package com.sprint.mission.discodeit.util.validation;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.RegionCode;
import com.sprint.mission.discodeit.enums.UserType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import io.micrometer.common.util.StringUtils;

public class UserValidator implements Validator<User, UserDTO.request> {
    @Override
    public void validateCreate(UserDTO.request entity) {
        if (!ValidatorExp.USERNAME.matches(entity.userName())) {
            throw new CustomException(ErrorCode.INVALID_USERNAME);
        }

        if (!ValidatorExp.NICKNAME.matches(entity.nickname())) {
            throw new CustomException(ErrorCode.INVALID_NICKNAME);
        }

        if (!ValidatorExp.PASSWORD.matches(entity.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (!ValidatorExp.PHONE.matches(entity.phone())) {
            throw new CustomException(ErrorCode.INVALID_PHONENUM);
        }

        if (!ValidatorExp.EMAIL.matches(entity.email())) {
            throw new CustomException(ErrorCode.INVALID_EMAIL);
        }

        if (StringUtils.isBlank(entity.regionCode().name())) {
            throw new CustomException(ErrorCode.REGION_CODE_IS_NOT_NULL);
        }
    }

    @Override
    public User validateUpdate(User current, UserDTO.request update) {
        boolean isUpdated = false;
        if (update.userName() != null && !update.userName().equals(current.getUserName().getName()) && ValidatorExp.USERNAME.matches(update.userName())) {
            current.updateUserName(update.userName());
            isUpdated = true;
        }

        if (update.nickname() != null && !update.nickname().equals(current.getNickname().getName()) && ValidatorExp.NICKNAME.matches(update.nickname())) {
            current.updateNickname(update.nickname());
            isUpdated = true;
        }

        if (update.email() != null && !update.email().equals(current.getEmail().getEmail()) && ValidatorExp.EMAIL.matches(update.email())) {
            current.updateEmail(update.email());
            isUpdated = true;
        }

        if (update.userType() != null && (update.userType() != UserType.fromString(current.getUserType().toString().toUpperCase()))) {
            current.updateUserType(UserType.fromString(update.userType().toString().toUpperCase()));
            isUpdated = true;
        }

        // phone
        if ((update.regionCode() != null && update.phone() != null)
                && (!update.phone().equals(current.getPhone().getPhone()) || update.regionCode() != RegionCode.fromString(current.getPhone().getRegionCode().toString().toUpperCase()))
                && ValidatorExp.PHONE.matches(update.phone())) {
            current.updatePhone(update.phone(), update.regionCode().toString().toUpperCase());
            isUpdated = true;
        }

        if (update.introduce() != null && !update.introduce().equals(current.getIntroduce())) {
            current.updateIntroduce(update.introduce());
            isUpdated = true;
        }
        return isUpdated ? current : null;
    }
}
