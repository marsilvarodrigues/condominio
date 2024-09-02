package test.com.pmrodrigues.condominio.services;


import com.pmrodrigues.condominio.dto.ApartamentoDTO;
import com.pmrodrigues.condominio.dto.MoradorResponseDTO;
import com.pmrodrigues.condominio.dto.UsuarioDTO;
import com.pmrodrigues.condominio.dto.VisitanteDTO;
import com.pmrodrigues.condominio.models.*;
import com.pmrodrigues.condominio.repositories.ApartamentoRepository;
import com.pmrodrigues.condominio.repositories.MoradorRepository;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import com.pmrodrigues.condominio.repositories.VisitanteRepository;
import com.pmrodrigues.condominio.services.VisitanteService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestVisitanteService {

    @Mock
    private VisitanteRepository visitanteRepository;

    @Mock
    private ApartamentoRepository apartamentoRepository;

    @Mock
    private MoradorRepository moradorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private VisitanteService visitanteService;


    @Test
    public void deveInformarAEntradaDeUmVisitante() {

        val apartamento = new Apartamento();
        apartamento.setBloco(new Bloco());

        val visitante = new VisitanteDTO(null, "nome do visitante",
                LocalDateTime.now(),
                new ApartamentoDTO(UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        null,
                        null),
                new UsuarioDTO(UUID.randomUUID().toString(), null),
                new MoradorResponseDTO(UUID.randomUUID().toString(), null, null, null, null, null )
        );
        when(apartamentoRepository.findByGuid(any(String.class))).thenReturn(Optional.of(apartamento));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(new Morador()));
        when(usuarioRepository.findByGuid(any(String.class))).thenReturn(Optional.of(new Usuario()));

        val saved = visitanteService.informarEntradaDeVisitante(visitante);
        assertThat(saved.visitaId()).isNotNull();

        verify(visitanteRepository).save(any(Visitante.class));

    }
}
