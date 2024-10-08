package test.com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.MoradorRequestDTO;
import com.pmrodrigues.condominio.dto.TelefoneDTO;
import com.pmrodrigues.condominio.models.*;
import com.pmrodrigues.condominio.repositories.ApartamentoRepository;
import com.pmrodrigues.condominio.repositories.MoradorRepository;
import com.pmrodrigues.condominio.repositories.TelefoneRepository;
import com.pmrodrigues.condominio.services.MoradorService;
import com.pmrodrigues.condominio.utilities.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestMoradorService {

    @Mock
    private MoradorRepository moradorRepository;

    @Mock
    private ApartamentoRepository apartamentoRepository;

    @Mock
    private TelefoneRepository telefoneRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private MoradorService service;


    @Test
    void deveCadastradarMorador() {

        val morador = Morador.builder().apartamento(
                        Apartamento.builder().bloco(
                                Bloco.builder().condominio(
                                        Condominio.builder().build()
                                ).build()
                        ).build()
                ).email("teste@teste.com")
                .build();

        when(emailService.to(anyString())).thenReturn(emailService);
        when(emailService.subject(anyString())).thenReturn(emailService);
        when(emailService.text(anyString())).thenReturn(emailService);

        when(apartamentoRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador.getApartamento()));
        when(moradorRepository.save(any(Morador.class))).thenReturn(morador);

        service.cadastrarNovoMorador(new MoradorRequestDTO(null,
                "email",
                UUID.randomUUID().toString(),
                "morador",
                "cpf",
                LocalDate.now(),
                List.of(new TelefoneDTO(null, "12345"))));

        verify(emailService).send();

    }

    @Test
    void deveAtualizarDadosMoradorAdicionandoNumeroNovo() {

        when(apartamentoRepository.findByGuid(anyString())).thenReturn(Optional.of(new Apartamento()));
        when(moradorRepository.findByGuid(anyString())).thenReturn(Optional.of(new Morador()));

        service.atualizarDadosMorador(new MoradorRequestDTO(UUID.randomUUID().toString(),
                "email",
                UUID.randomUUID().toString(),
                "morador",
                "cpf",
                LocalDate.now(),
                List.of(new TelefoneDTO(null, "12345"))));

        verify(moradorRepository).save(any(Morador.class));

    }

    @Test
    void deveAtualizarDadosDoMoradorAtualizandoTelefone() {

        val telefoneId = UUID.randomUUID().toString();
        val morador = Morador.builder().build();
        morador.adicionarTelefone(Telefone.builder().guid(telefoneId).build());


        when(apartamentoRepository.findByGuid(anyString())).thenReturn(Optional.of(new Apartamento()));
        when(moradorRepository.findByGuid(anyString())).thenReturn(Optional.of(morador));
        when(telefoneRepository.findByGuid(anyString())).thenReturn(Optional.of(Telefone.builder().guid(telefoneId).build()));

        service.atualizarDadosMorador(new MoradorRequestDTO(UUID.randomUUID().toString(),
                "email",
                UUID.randomUUID().toString(),
                "morador",
                "cpf",
                LocalDate.now(),
                List.of(new TelefoneDTO(telefoneId, "12345"))));

        verify(moradorRepository).save(any(Morador.class));

    }
}

