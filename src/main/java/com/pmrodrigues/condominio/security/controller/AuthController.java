package com.pmrodrigues.condominio.security.controller;

import com.pmrodrigues.condominio.security.JwtTokenProvider;
import com.pmrodrigues.condominio.security.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<JwtResponse> authenticate(@RequestParam String username, @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            String token = jwtTokenProvider.generateToken(authentication.getName());

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException e) {
            log.error("erro ao gerar o token jwt {}", e.getMessage(), e);
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
    }


}
