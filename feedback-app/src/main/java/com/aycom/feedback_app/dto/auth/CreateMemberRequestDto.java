package com.aycom.feedback_app.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMemberRequestDto {

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Customer email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull(message = "Password is required")
    private String password;
}
