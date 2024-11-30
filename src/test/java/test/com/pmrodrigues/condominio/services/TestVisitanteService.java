package test.com.pmrodrigues.condominio.services;


import com.pmrodrigues.condominio.dto.*;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        val visitante = new VisitanteRequestDTO(null, new Date(),"nome do visitante",
                new VeiculoDTO(UUID.randomUUID().toString(), "FIT", "BRANCA", "LRH6605", "HONDA"),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString() );

        when(apartamentoRepository.findByGuid(any(String.class))).thenReturn(Optional.of(apartamento));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(new Morador()));
        when(usuarioRepository.findByGuid(any(String.class))).thenReturn(Optional.of(new Usuario()));

        val saved = visitanteService.informarEntradaDeVisitante(visitante);
        assertThat(saved.visitaId()).isNotNull();

        verify(visitanteRepository).save(any(Visitante.class));

    }

    @Test
    public void deveInformarAEntradaDeUmVisitanteComVeiculo() {
        val apartamento = new Apartamento();
        apartamento.setBloco(new Bloco());

        val visitante = new VisitanteRequestDTO(null, new Date(),"nome do visitante",
                new VeiculoDTO(UUID.randomUUID().toString(), "FIT", "BRANCA", "LRH6605", "HONDA"),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString() );

        when(apartamentoRepository.findByGuid(any(String.class))).thenReturn(Optional.of(apartamento));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(new Morador()));
        when(usuarioRepository.findByGuid(any(String.class))).thenReturn(Optional.of(new Usuario()));

        val saved = visitanteService.informarEntradaDeVisitante(visitante);
        assertThat(saved.visitaId()).isNotNull();

        verify(visitanteRepository).save(any(Visitante.class));
    }

    @Test
    void devePesquisarVisitantesPorApartamento() {

        val visitante = new Visitante();
        visitante.setRegistradoPor(new Usuario());
        visitante.setAutorizadoPor(new Morador());

        when(apartamentoRepository.findByGuid(anyString())).thenReturn(Optional.of(new Apartamento()));
        when(visitanteRepository.findByApartamento(any(Apartamento.class))).thenReturn(List.of(visitante));

        val visitas = visitanteService.recuperarVisitantesPorApartamento(new ApartamentoDTO(UUID.randomUUID().toString(), null, null, null));
        assertThat(visitas.isEmpty()).isFalse();

    }
}
