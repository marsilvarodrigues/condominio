package com.pmrodrigues.condominio.security.controller;

import com.pmrodrigues.condominio.security.dto.JwtResponse;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Value("${jwt.secret}")
    @Setter
    private String secretKey;

    @Setter
    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey.replace("-","."));
        return Keys.hmacShaKeyFor(keyBytes);
    }

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
                    .signWith(SignatureAlgorithm.HS512, this.getSigningKey())
                    .compact();

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException e) {
            log.error("erro ao gerar o token jwt {}", e.getMessage(), e);
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
    }


}
