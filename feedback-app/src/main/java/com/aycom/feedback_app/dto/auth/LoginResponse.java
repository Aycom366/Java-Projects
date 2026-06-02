package com.aycom.feedback_app.dto.auth;

import lombok.Builder;

@Builder
public record LoginResponse(
        String token, String email) {

}
