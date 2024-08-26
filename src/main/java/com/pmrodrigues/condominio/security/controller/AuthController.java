package com.pmrodrigues.condominio.security.controller;

import com.pmrodrigues.condominio.security.dto.JwtResponse;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import io.jsonwebtoken.Jwts;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Value("${jwt.secret}")
    @Setter
    private String secretKey;

    @Setter
    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    @PostMapping(consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE },
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<?> authenticate(@RequestParam String username, @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            String token = Jwts.builder()
                    .setSubject(authentication.getName())
                    .setExpiration(new Date(System.currentTimeMillis() + this.validityInMilliseconds))
                    .signWith(SignatureAlgorithm.HS256, this.secretKey.getBytes())
                    .compact();

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Credenciais inválidas");
        }
    }


}
