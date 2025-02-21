package com.sprint.mission.discodeit.dto.request.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserCreateDTO(
    @NotBlank(message = "Username is required") String username,
    @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
    @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 char") String password
) {

}
