package test.com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.models.Usuario;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import com.pmrodrigues.condominio.services.UserDetailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestUserDetailService {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Test
    void testLoadUserByUsername_UserExists() {
        // Dado
        String username = "testuser";
        Usuario usuario = new Usuario(); // Ajuste conforme o seu modelo
        usuario.setUsername(username);
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));

        // Quando
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        // Então
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        // Dado
        String username = "nonexistentuser";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());


        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailService.loadUserByUsername(username);
        });
    }
}
