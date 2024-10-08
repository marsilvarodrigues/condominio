package test.com.pmrodrigues.condominio.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pmrodrigues.condominio.controller.ReservaController;
import com.pmrodrigues.condominio.controller.VisitanteController;
import com.pmrodrigues.condominio.dto.*;
import com.pmrodrigues.condominio.exceptions.ApartamentoNotFoundException;
import com.pmrodrigues.condominio.services.VisitanteService;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = { WebSecurityConfiguration.class })
@ContextConfiguration(classes = {
        VisitanteController.class
}
)
@AutoConfigureMockMvc(addFilters = false)
public class TestVisitanteController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VisitanteService service;


    @BeforeEach
    public void beforeEach() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @SneakyThrows
    void deveRegistrarAEntradaDeVisitanteComVeiculo() {

        var request = new VisitanteRequestDTO(null, LocalDateTime.now(),
                "/visitante",
                new VeiculoDTO(null, null ,null, null, null),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        when(service.informarEntradaDeVisitante(any(VisitanteRequestDTO.class))).
                thenReturn(new VisitanteResponseDTO(UUID.randomUUID().toString(),
                        "visitante",
                        LocalDateTime.now(),
                        new VeiculoDTO(null, null, null, null ,null ),
                        new ApartamentoDTO(null ,null ,null ,null),
                        new UsuarioDTO(null, null ),
                        new MoradorResponseDTO(null, null ,null ,null ,null ,null)
                        ));

        mvc.perform(post("/visitantes")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    @SneakyThrows
    void deveRegistrarAEntradaDeVisitanteSemVeiculo() {

        var request = new VisitanteRequestDTO(null, LocalDateTime.now(),
                "/visitante",
                null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        when(service.informarEntradaDeVisitante(any(VisitanteRequestDTO.class))).
                thenReturn(new VisitanteResponseDTO(UUID.randomUUID().toString(),
                        "visitante",
                        LocalDateTime.now(),
                        null,
                        new ApartamentoDTO(null ,null ,null ,null),
                        new UsuarioDTO(null, null ),
                        new MoradorResponseDTO(null, null ,null ,null ,null ,null)
                ));

        mvc.perform(post("/visitantes")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    @SneakyThrows
    void deveListarVisitantesPorApartamento() {
        val visitas = new ArrayList<VisitanteResponseDTO>();
        when(service.recuperarVisitantesPorApartamento(any(ApartamentoDTO.class))).thenReturn(visitas);

        mvc.perform(get("/visitantes?apartamento=" + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @SneakyThrows
    void naoPodePesquisarOsVisitantesApartamentoNaoEncontrado() {
        when(service.recuperarVisitantesPorApartamento(any(ApartamentoDTO.class))).thenThrow(ApartamentoNotFoundException.class);

        mvc.perform(get("/visitantes?apartamento=" + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }


}
