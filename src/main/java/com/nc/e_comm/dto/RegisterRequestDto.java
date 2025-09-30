package com.nc.e_comm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    @NotBlank
    private String fullName;
    @Email
    private String email;

    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;

    private Boolean termsAccepted;

}
