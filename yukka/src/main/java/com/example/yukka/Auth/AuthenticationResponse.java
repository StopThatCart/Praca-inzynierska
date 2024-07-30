package com.example.yukka.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
class AuthenticationResponse {
    private String token;
}
