package test.com.pmrodrigues.condominio.security.controllers;

import com.pmrodrigues.condominio.security.controller.AuthController;
import com.pmrodrigues.condominio.security.dto.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestAuthController {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private String secretKey = UUID.randomUUID().toString();

    private long validityInMilliseconds = 3600000; // 1 hour

    @BeforeEach
    void beforeEach() {
        authController.setSecretKey(this.secretKey);
        authController.setValidityInMilliseconds(this.validityInMilliseconds);
    }

    @Test
    void testAuthenticateSuccess() {
        // Arrange
        String username = "user";
        String password = "password";
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();


        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Act
        ResponseEntity<?> response = authController.authenticate(username, password);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);

    }

    @Test
    void testAuthenticateFailure() {
        // Arrange
        String username = "user";
        String password = "password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        // Act
        ResponseEntity<?> response = authController.authenticate(username, password);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciais inválidas", response.getBody());
    }
}
