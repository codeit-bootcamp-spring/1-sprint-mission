package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
	@NotBlank(message = "사용자 이름은 필수입니다.")
	@Size(max = 50, message = "사용자 이름은 최대 50자까지 가능합니다.")
	String username,

	/*
	Email 어노테이션 정규표현식 default가 .*라 모든 표현식을 허용하여 따로 정규 표현식 작성
	이메일 아이디는 알파벳 대소문자, 숫자, 점(.), 밑줄(_), 퍼센트(%), 더하기(+), 빼기(-)만 허용
	"@" 기호가 반드시 있어야 함
	도메인 이름은 알파벳, 숫자, 점(.) 및 하이픈(-)을 포함할 수 있음
	도메인 확장자는 알파벳 2자 이상이어야 하며, 반드시 점(.) 뒤에 와야 함
	*/
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효한 이메일 주소여야 합니다.")
	@Size(max = 100, message = "이메일은 최대 100자까지 가능합니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(max = 60, message = "비밀번호는 최대 60자까지 가능합니다.")
	String password
) {
}
