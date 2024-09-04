package com.sopra.UserManagement.response;

import lombok.*;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String jwt;
    private String message;


    public void setJwt(String token) {
    }

    public void setMessage(String s) {
        
    }
}
