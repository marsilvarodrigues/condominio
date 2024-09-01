package test.com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.EspacoComumDTO;
import com.pmrodrigues.condominio.dto.ReservaRequestDTO;
import com.pmrodrigues.condominio.enums.StatusReserva;
import com.pmrodrigues.condominio.exceptions.AlteracaoNaoPermitadaException;
import com.pmrodrigues.condominio.exceptions.EspacoBloqueadoParaException;
import com.pmrodrigues.condominio.exceptions.EstouroDeReservasAtivasException;
import com.pmrodrigues.condominio.exceptions.ForaDoPrazoParaReservarException;
import com.pmrodrigues.condominio.models.EspacoComum;
import com.pmrodrigues.condominio.models.Morador;
import com.pmrodrigues.condominio.models.Reserva;
import com.pmrodrigues.condominio.repositories.EspacoComumRepository;
import com.pmrodrigues.condominio.repositories.MoradorRepository;
import com.pmrodrigues.condominio.repositories.ReservaRepository;
import com.pmrodrigues.condominio.services.ReservaService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestReservaService {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private EspacoComumRepository espacoComumRepository;

    @Mock
    private MoradorRepository moradorRepository;

    @Test
    public void deveEfetuarReserva() {

        val espacoComum = mock(EspacoComum.class);
        val morador = mock(Morador.class);

        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.quantasReservasAtivasExistemParaOMorador(any(Morador.class))).thenReturn(0L);
        when(reservaRepository.findByEspacoComumDataReserva(any(EspacoComum.class), any(LocalDate.class), any(StatusReserva.class))).thenReturn(Optional.empty());

        val saved = reservaService.efetuarReserva(new ReservaRequestDTO(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), LocalDate.now().minusDays(1), null));

        verify(reservaRepository).save(any(Reserva.class));
        assertThat(saved.reservaId()).isNotNull();
        assertThat(saved.statusReserva()).isEqualTo(StatusReserva.RESERVADO);
    }

    @Test
    public void soPodeReservarCom24DeAntecedencia() {
        assertThrows(ForaDoPrazoParaReservarException.class, () ->
                reservaService.efetuarReserva(new ReservaRequestDTO(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), LocalDate.now(), null))
                );
    }

    @Test
    public void podeEfetuarUmCancelamentoDeReserva() {

        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();


        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(LocalDate.now().plusDays(1));


        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.findByGuid(any(String.class))).thenReturn(Optional.of(reserva));

        val saved = reservaService.cancelarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), reserva.getDataReserva(), reserva.getStatusReserva()));
        assertThat(saved.statusReserva()).isEqualTo(StatusReserva.CANCELADO);

        verify(reservaRepository).save(any(Reserva.class));

    }

    @Test
    public void soPodeEfetuarDuasReservasAtivas() {

        val espacoComum = mock(EspacoComum.class);
        val morador = mock(Morador.class);

        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.quantasReservasAtivasExistemParaOMorador(any(Morador.class))).thenReturn(2L);

        assertThrows(EstouroDeReservasAtivasException.class, () ->
                reservaService.efetuarReserva(new ReservaRequestDTO(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), LocalDate.now().minusDays(1), null))
        );

    }

    @Test
    public void naoPodeReservarEspacoJaReservado() {

        val espacoComum = mock(EspacoComum.class);
        val morador = mock(Morador.class);
        val reserva = mock(Reserva.class);

        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.quantasReservasAtivasExistemParaOMorador(any(Morador.class))).thenReturn(0L);
        when(reservaRepository.findByEspacoComumDataReserva(any(EspacoComum.class), any(LocalDate.class), any(StatusReserva.class))).thenReturn(Optional.of(reserva));

        assertThrows(EspacoBloqueadoParaException.class, () ->
                reservaService.efetuarReserva(new ReservaRequestDTO(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), LocalDate.now().minusDays(1), null))
        );

    }

    @Test
    public void naoPodeCancelarReservaComMenosDe24Antecedencia() {

        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(LocalDate.now());

        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.findByGuid(any(String.class))).thenReturn(Optional.of(reserva));

        assertThrows(ForaDoPrazoParaReservarException.class, () -> reservaService.cancelarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), reserva.getDataReserva(), reserva.getStatusReserva())));


    }


    @Test
    public void podeAlterarUmaReserva() {

        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(LocalDate.now().plusDays(1));


        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.findByGuid(any(String.class))).thenReturn(Optional.of(reserva));

        val saved = reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), reserva.getDataReserva().plusDays(5), reserva.getStatusReserva()));
        assertThat(saved.dataReserva()).isEqualTo(LocalDate.now().plusDays(6));

        verify(reservaRepository).save(any(Reserva.class));

    }

    @Test
    public void naoPodeAlterarUmaReservaComMenosDe24Horas() {

        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(LocalDate.now());


        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.findByGuid(any(String.class))).thenReturn(Optional.of(reserva));

        assertThrows(ForaDoPrazoParaReservarException.class, () -> reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), reserva.getDataReserva().plusDays(5), reserva.getStatusReserva())));

    }

    @Test
    public void soPodeAlterarReservasEmReserva() {
        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(LocalDate.now());
        reserva.setStatusReserva(StatusReserva.PAGO);

        assertThrows(AlteracaoNaoPermitadaException.class, () -> reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), reserva.getDataReserva().plusDays(5), reserva.getStatusReserva())));
    }

    @Test
    public void naoPodeAlterarSeAproximaDataEstiverReservada() {

        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(LocalDate.now().plusDays(1));

        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.findByGuid(any(String.class))).thenReturn(Optional.of(reserva));
        when(reservaRepository.findByEspacoComumDataReserva(any(EspacoComum.class), any(LocalDate.class), any(StatusReserva.class))).thenReturn(Optional.of(new Reserva()));

        assertThrows(EspacoBloqueadoParaException.class, () ->
                reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), reserva.getDataReserva().plusDays(5), reserva.getStatusReserva()))
        );

    }

    @Test
    public void soPodeCancelarReservasEmReserva() {
        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(LocalDate.now());
        reserva.setStatusReserva(StatusReserva.PAGO);

        assertThrows(AlteracaoNaoPermitadaException.class, () -> reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), reserva.getDataReserva().plusDays(5), reserva.getStatusReserva())));
    }

    @Test
    public void deveListarTodasAsReservasAbertarParaUmDeterminadoEspacoComum() {

        val reservas = mock(List.class);
        val espacoComum = new EspacoComum();
        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(reservaRepository.findByEspacoComum(any(EspacoComum.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(reservas);

        val founded = reservaService.pesquisarReservasPorEspacoComum(new EspacoComumDTO(UUID.randomUUID().toString(), null, null, 10));
        assertThat(founded).isNotNull();

    }
}
