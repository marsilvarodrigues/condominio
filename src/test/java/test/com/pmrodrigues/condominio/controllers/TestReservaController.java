package test.com.pmrodrigues.condominio.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pmrodrigues.condominio.controller.ReservaController;
import com.pmrodrigues.condominio.dto.EspacoComumDTO;
import com.pmrodrigues.condominio.dto.ReservaRequestDTO;
import com.pmrodrigues.condominio.dto.ReservaResponseDTO;
import com.pmrodrigues.condominio.enums.StatusReserva;
import com.pmrodrigues.condominio.exceptions.*;
import com.pmrodrigues.condominio.models.EspacoComum;
import com.pmrodrigues.condominio.services.ReservaService;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = { WebSecurityConfiguration.class })
@ContextConfiguration(classes = {
        ReservaController.class
}
         )
@AutoConfigureMockMvc(addFilters = false)
public class TestReservaController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservaService service;


    @BeforeEach
    public void beforeEach() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @SneakyThrows
    void deveCriarUmaReserva() {

        val request = new ReservaRequestDTO(null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                StatusReserva.RESERVADO);

        when(service.efetuarReserva(any(ReservaRequestDTO.class))).thenReturn(new ReservaResponseDTO(null, null, null, null, null ));

        mvc.perform(post("/reservas")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

    }


    @Test
    @SneakyThrows
    void naoPodeCriarAReservaEspacaoBloqueado() {

        val request = new ReservaRequestDTO(null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                StatusReserva.RESERVADO);

        when(service.efetuarReserva(any(ReservaRequestDTO.class))).
                thenThrow(EspacoBloqueadoParaException.class);

        mvc.perform(post("/reservas")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    @SneakyThrows
    void naoPodeCriarAReservaForaDoPrazoParaReservaException() {

        val request = new ReservaRequestDTO(null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                StatusReserva.RESERVADO);

        when(service.efetuarReserva(any(ReservaRequestDTO.class))).
                thenThrow(ForaDoPrazoParaReservarException.class);

        mvc.perform(post("/reservas")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    @SneakyThrows
    void naoPodeEfetuarAReservaEstourouAQuantidadeMaximaDeReservasPermitidas() {

        val request = new ReservaRequestDTO(null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                StatusReserva.RESERVADO);

        when(service.efetuarReserva(any(ReservaRequestDTO.class))).
                thenThrow(EstouroDeReservasAtivasException.class);

        mvc.perform(post("/reservas")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    @SneakyThrows
    void deveAlterarUmReserva() {

        val request = new ReservaRequestDTO(null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                StatusReserva.RESERVADO);

        when(service.alterarReserva(any(ReservaRequestDTO.class))).thenReturn(new ReservaResponseDTO(null, null, null, null, null ));

        mvc.perform(put("/reservas/" + UUID.randomUUID().toString())
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @SneakyThrows
    void naoPodeAlterarReservaNaoEncontrada() {

        val request = new ReservaRequestDTO(null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                StatusReserva.RESERVADO);

        when(service.alterarReserva(any(ReservaRequestDTO.class))).thenThrow(ReservaNotFoundException.class);

        mvc.perform(put("/reservas/" + UUID.randomUUID().toString())
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    @SneakyThrows
    void naoPodeAlterarReservaEstouroQuantidadeMaximaDeReservas() {

        val request = new ReservaRequestDTO(null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                StatusReserva.RESERVADO);

        when(service.alterarReserva(any(ReservaRequestDTO.class))).thenThrow(EstouroDeReservasAtivasException.class);

        mvc.perform(put("/reservas/" + UUID.randomUUID().toString())
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    @SneakyThrows
    public void deveListarReservasPorEspacoComum() {
        val reservas = new ArrayList<ReservaResponseDTO>();
        when(service.pesquisarReservasPorEspacoComum(any(EspacoComumDTO.class))).thenReturn(reservas);

        mvc.perform(get("/reservas?espacoComum=" + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @SneakyThrows
    public void naoPodelistarReservasEspacoComumNaoEncontrado() {

        when(service.pesquisarReservasPorEspacoComum(any(EspacoComumDTO.class))).thenThrow(EspacoComumNotFoundException.class);

        mvc.perform(get("/reservas?espacoComum=" + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

    }
}



