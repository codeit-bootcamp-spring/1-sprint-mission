package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class RegionCodeCanNotNullException extends UserException {
    public RegionCodeCanNotNullException() {
        super(ErrorCode.REGION_CODE_IS_NOT_NULL);
    }
}
