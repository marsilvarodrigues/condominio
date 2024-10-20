package test.com.pmrodrigues.condominio.security.controllers;

import com.pmrodrigues.condominio.security.JwtTokenProvider;
import com.pmrodrigues.condominio.security.controller.AuthController;
import com.pmrodrigues.condominio.security.dto.JwtResponse;
import io.jsonwebtoken.Claims;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestAuthController {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @Test
    void testAuthenticateSuccess() {
        // Arrange
        String username = "user";
        String password = "password";
        val claims = mock(Claims.class);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        when(authenticationManager.authenticate(authentication))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(anyString())).thenReturn("");

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
        ResponseEntity<JwtResponse> response = authController.authenticate(username, password);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }
}
