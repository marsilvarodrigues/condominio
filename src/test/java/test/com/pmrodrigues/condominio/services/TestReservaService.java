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
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
        when(reservaRepository.findByEspacoComumDataReserva(any(EspacoComum.class), any(Date.class), any(StatusReserva.class))).thenReturn(Optional.empty());

        val saved = reservaService.efetuarReserva(new ReservaRequestDTO(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()), null));

        verify(reservaRepository).save(any(Reserva.class));
        assertThat(saved.reservaId()).isNotNull();
        assertThat(saved.statusReserva()).isEqualTo(StatusReserva.RESERVADO);
    }

    @Test
    public void soPodeReservarCom24DeAntecedencia() {
        assertThrows(ForaDoPrazoParaReservarException.class, () ->
                reservaService.efetuarReserva(new ReservaRequestDTO(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), new Date(), null))
                );
    }

    @Test
    public void podeEfetuarUmCancelamentoDeReserva() {

        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();


        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));


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
                reservaService.efetuarReserva(new ReservaRequestDTO(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()), null))
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
        when(reservaRepository.findByEspacoComumDataReserva(any(EspacoComum.class), any(Date.class), any(StatusReserva.class))).thenReturn(Optional.of(reserva));

        assertThrows(EspacoBloqueadoParaException.class, () ->
                reservaService.efetuarReserva(new ReservaRequestDTO(null, UUID.randomUUID().toString(), UUID.randomUUID().toString(), Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()), null))
        );

    }

    @Test
    public void naoPodeCancelarReservaComMenosDe24Antecedencia() {

        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(new Date());

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
        reserva.setDataReserva(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));


        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.findByGuid(any(String.class))).thenReturn(Optional.of(reserva));

        Date dataReserva = DateUtils.addDays(reserva.getDataReserva(),5);

        val saved = reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), dataReserva, reserva.getStatusReserva()));
        assertThat(saved.dataReserva()).isEqualTo(Date.from(
                LocalDate.now()
                        .plusDays(6)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant())
        );

        verify(reservaRepository).save(any(Reserva.class));

    }

    @Test
    public void naoPodeAlterarUmaReservaComMenosDe24Horas() {

        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));


        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.findByGuid(any(String.class))).thenReturn(Optional.of(reserva));

        assertThrows(ForaDoPrazoParaReservarException.class, () -> reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), DateUtils.addDays(reserva.getDataReserva(), 1), reserva.getStatusReserva())));

    }

    @Test
    public void soPodeAlterarReservasEmReserva() {
        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(new Date());
        reserva.setStatusReserva(StatusReserva.PAGO);

        assertThrows(AlteracaoNaoPermitadaException.class, () -> reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), DateUtils.addDays(reserva.getDataReserva(), 5), reserva.getStatusReserva())));
    }

    @Test
    public void naoPodeAlterarSeAproximaDataEstiverReservada() {

        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(DateUtils.addDays(new Date(), 1));

        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(moradorRepository.findByGuid(any(String.class))).thenReturn(Optional.of(morador));
        when(reservaRepository.findByGuid(any(String.class))).thenReturn(Optional.of(reserva));
        when(reservaRepository.findByEspacoComumDataReserva(any(EspacoComum.class), any(Date.class), any(StatusReserva.class))).thenReturn(Optional.of(new Reserva()));

        assertThrows(EspacoBloqueadoParaException.class, () ->
                reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), DateUtils.addDays(reserva.getDataReserva(),5), reserva.getStatusReserva()))
        );

    }

    @Test
    public void soPodeCancelarReservasEmReserva() {
        val espacoComum = new EspacoComum();
        val morador = new Morador();
        val reserva = new Reserva();

        reserva.setEspacoComum(espacoComum);
        reserva.setMorador(morador);
        reserva.setDataReserva(new Date());
        reserva.setStatusReserva(StatusReserva.PAGO);

        assertThrows(AlteracaoNaoPermitadaException.class, () -> reservaService.alterarReserva(new ReservaRequestDTO(reserva.getGuid(), espacoComum.getGuid(), morador.getGuid(), DateUtils.addDays(reserva.getDataReserva(),5), reserva.getStatusReserva())));
    }

    @Test
    public void deveListarTodasAsReservasAbertarParaUmDeterminadoEspacoComum() {

        val reservas = mock(List.class);
        val espacoComum = new EspacoComum();
        when(espacoComumRepository.findByGuid(any(String.class))).thenReturn(Optional.of(espacoComum));
        when(reservaRepository.findByEspacoComum(any(EspacoComum.class), any(Date.class), any(Date.class))).thenReturn(reservas);

        val founded = reservaService.pesquisarReservasPorEspacoComum(new EspacoComumDTO(UUID.randomUUID().toString(), null, null, 10));
        assertThat(founded).isNotNull();

    }
}
