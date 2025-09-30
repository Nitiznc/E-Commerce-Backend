package com.nc.e_comm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
