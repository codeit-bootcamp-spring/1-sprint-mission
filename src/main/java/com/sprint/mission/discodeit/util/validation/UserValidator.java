package com.sprint.mission.discodeit.util.validation;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.RegionCodeCanNotNullException;
import com.sprint.mission.discodeit.exception.user.UserValidationException;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

@Component("userValidator")
public class UserValidator implements Validator<User, UserSignupRequestDto, UserUpdateDto> {
    @Override
    public void validateCreate(UserSignupRequestDto entity) {
        if (!ValidatorExp.USERNAME.matches(entity.username())) {
            throw new UserValidationException(ErrorCode.INVALID_USERNAME, entity.username());
        }

        if (!ValidatorExp.NICKNAME.matches(entity.nickname())) {
            throw new UserValidationException(ErrorCode.INVALID_NICKNAME, entity.nickname());
        }

        if (!ValidatorExp.PASSWORD.matches(entity.password())) {
            throw new UserValidationException(ErrorCode.INVALID_PASSWORD, entity.password());
        }

        if (!ValidatorExp.PHONE.matches(entity.phone())) {
            throw new UserValidationException(ErrorCode.INVALID_PHONENUM, entity.phone());
        }

        if (!ValidatorExp.EMAIL.matches(entity.email())) {
            throw new UserValidationException(ErrorCode.INVALID_EMAIL, entity.email());
        }

        if (StringUtils.isBlank(entity.regionCode().name())) {
            throw new RegionCodeCanNotNullException();
        }
    }

    @Override
    public User validateUpdate(User current, UserUpdateDto update) {
        boolean isUpdated = false;
        if (update.username() != null && !update.username().equals(current.getUsername()) && ValidatorExp.USERNAME.matches(update.username())) {
            current.updateUsername(update.username());
            isUpdated = true;
        }

        if (update.nickname() != null && !update.nickname().equals(current.getNickname()) && ValidatorExp.NICKNAME.matches(update.nickname())) {
            current.updateNickname(update.nickname());
            isUpdated = true;
        }

        if (update.email() != null && !update.email().equals(current.getEmail()) && ValidatorExp.EMAIL.matches(update.email())) {
            current.updateEmail(update.email());
            isUpdated = true;
        }

        if (update.userType() != null && (update.userType() != User.UserType.fromString(current.getUserType().toString().toUpperCase()))) {
            current.updateUserType(User.UserType.fromString(update.userType().toString().toUpperCase()));
            isUpdated = true;
        }

        // phone
        if ((update.regionCode() != null && update.phone() != null)
                && (!update.phone().equals(current.getPhone().getPhone()) || update.regionCode() != Phone.RegionCode.fromString(current.getPhone().getRegionCode().toString().toUpperCase()))
                && ValidatorExp.PHONE.matches(update.phone())) {
            current.updatePhone(new Phone(update.phone(), update.regionCode()));
            isUpdated = true;
        }

        if (update.introduce() != null && !update.introduce().equals(current.getIntroduce())) {
            current.updateIntroduce(update.introduce());
            isUpdated = true;
        }
        return isUpdated ? current : null;
    }
}
