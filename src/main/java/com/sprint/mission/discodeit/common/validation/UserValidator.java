package com.sprint.mission.discodeit.common.validation;

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
        if (update.userName() != null && !current.getUserName().getName().equals(update.userName()) && ValidatorExp.USERNAME.matches(update.userName())) {
            current.updateUserName(update.userName());
            isUpdated = true;
        }

        if (update.nickname() != null && !current.getNickname().getName().equals(update.nickname()) && ValidatorExp.NICKNAME.matches(update.nickname())) {
            current.updateNickname(update.nickname());
            isUpdated = true;
        }

        if (update.email() != null && !current.getEmail().getEmail().equals(update.email()) && ValidatorExp.EMAIL.matches(update.email())) {
            current.updateEmail(update.email());
            isUpdated = true;
        }

        if (update.userType() != null && (current.getUserType() != UserType.fromString(update.userType().toString().toUpperCase()))) {
            current.updateUserType(UserType.fromString(update.userType().toString().toUpperCase()));
            isUpdated = true;
        }

        // phone
        if ((update.regionCode() != null && update.phone() != null)
                && (!current.getPhone().getPhone().equals(update.phone()) || current.getPhone().getRegionCode() != RegionCode.fromString(update.regionCode().toString().toUpperCase()))
                && ValidatorExp.PHONE.matches(update.phone())) {
            current.updatePhone(update.phone(), update.regionCode().toString().toUpperCase());
            isUpdated = true;
        }

        if (update.introduce() != null && !current.getIntroduce().equals(update.introduce())) {
            current.updateIntroduce(update.introduce());
            isUpdated = true;
        }
        return isUpdated ? current : null;
    }
}
