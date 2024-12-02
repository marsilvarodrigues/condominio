package test.com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.PerfilDTO;
import com.pmrodrigues.condominio.dto.UsuarioDTO;
import com.pmrodrigues.condominio.models.Usuario;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import com.pmrodrigues.condominio.services.UsuarioService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestUsuarioService {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;


    @Test
    void devePesquisarUsuario() {

        when(usuarioRepository.findAll(any(Specification.class))).thenReturn(List.of(new Usuario()));
        val usuarios = usuarioService.pesquisarUsuarios(new UsuarioDTO(1L,
                UUID.randomUUID().toString(),UUID.randomUUID().toString(),new Date(),
                List.of(new PerfilDTO(1L, UUID.randomUUID().toString(), ""))));

    }

}