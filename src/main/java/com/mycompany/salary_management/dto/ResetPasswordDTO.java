package com.mycompany.salary_management.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String token;
    private String newPassword;
}
