package test.com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.enums.StatusReserva;
import com.pmrodrigues.condominio.models.*;
import com.pmrodrigues.condominio.repositories.*;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static test.com.pmrodrigues.condominio.utils.GeradorCPF.gerarCPF;

@DataJpaTest
@ContextConfiguration(classes = UsuarioRepository.class)
@EnableJpaRepositories(basePackages = {"com.pmrodrigues.condominio.repositories"})
@EntityScan("com.pmrodrigues.condominio.models")
public class TestReservaRepository {

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private EspacoComumRepository espacoComumRepository;

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private ApartamentoRepository apartamentoRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    private Morador getMorador() {

        Morador morador = new Morador();
        morador.setNome("João da Silva");
        morador.setDataNascimento(LocalDate.of(1990, 1, 1));
        morador.setEmail("joao.silva@example.com");
        morador.setCpf(gerarCPF());
        morador.setUsername("username");
        morador.setPassword("password");
        morador.setApartamento(this.getApartamento());

        moradorRepository.save(morador);

        return morador;
    }

    private Apartamento getApartamento() {
        Condominio condominio = new Condominio();
        condominio.setNome("Condomínio Jardim das Flores");
        condominio.setQuantidadeBlocos(5L);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco A");
        bloco.setNumero("101");
        bloco.setQuantidadeApartamento(10L);

        Apartamento apartamento = new Apartamento();
        apartamento.setNumero("101");
        apartamento.setAndar(1L);
        bloco.adicionarApartamento(apartamento);

        condominio.adicionarBloco(bloco);

        condominioRepository.save(condominio);
        return apartamento;
    }

    @Test
    void testFindByGuid() {

        Condominio condominio = getApartamento().getBloco().getCondominio();

        condominioRepository.save(condominio);

        EspacoComum espacoComum = new EspacoComum();
        espacoComum.setNome("EspacoComum");
        espacoComum.setDescricao("Piscina");
        espacoComum.setCapacidadeMaxima(100);
        espacoComum.setCondominio(condominio);

        espacoComumRepository.save(espacoComum);

        Reserva reserva = new Reserva();
        reserva.setDataReserva(LocalDate.now());
        reserva.setMorador(this.getMorador());
        reserva.setStatusReserva(StatusReserva.RESERVADO);
        reserva.setEspacoComum(espacoComum);

        reservaRepository.save(reserva);

        String guid = reserva.getGuid();

        // Testar a busca por GUID
        Optional<Reserva> found = reservaRepository.findByGuid(guid);

        assertThat(found).isPresent();
        assertThat(found.get().getMorador()).isEqualTo(reserva.getMorador());

    }

    @Test
    void testFindByGuidNotFound() {
        // Testar a busca por GUID inexistente
        Optional<EspacoComum> found = espacoComumRepository.findByGuid("non-existing-guid");
        assertThat(found).isNotPresent();
    }

    @Test
    void deveRetornarReservasPorEspacoComumDataDaReservaEStatusDaReserva() {

        Condominio condominio = getApartamento().getBloco().getCondominio();
        Morador morador =getMorador();
        condominioRepository.save(condominio);

        EspacoComum espacoComum = new EspacoComum();
        espacoComum.setNome("EspacoComum");
        espacoComum.setDescricao("Piscina");
        espacoComum.setCapacidadeMaxima(100);
        espacoComum.setCondominio(condominio);

        espacoComumRepository.save(espacoComum);
        LocalDate dataReserva = LocalDate.now();

        Reserva reserva = new Reserva();
        reserva.setDataReserva(dataReserva);
        reserva.setMorador(morador);
        reserva.setStatusReserva(StatusReserva.RESERVADO);
        reserva.setEspacoComum(espacoComum);

        reservaRepository.save(reserva);

        val founded = reservaRepository.findByEspacoComumDataReserva(espacoComum, dataReserva, StatusReserva.RESERVADO);
        assertThat(founded.isPresent()).isTrue();
        assertThat(founded.get().getEspacoComum()).isEqualTo(espacoComum);

    }

    @Test
    void testCalculaAQuantidadeDeReservasAtivas() {

        Condominio condominio = getApartamento().getBloco().getCondominio();
        Morador morador =getMorador();
        condominioRepository.save(condominio);

        EspacoComum espacoComum = new EspacoComum();
        espacoComum.setNome("EspacoComum");
        espacoComum.setDescricao("Piscina");
        espacoComum.setCapacidadeMaxima(100);
        espacoComum.setCondominio(condominio);

        espacoComumRepository.save(espacoComum);

        Reserva reserva = new Reserva();
        reserva.setDataReserva(LocalDate.now());
        reserva.setMorador(morador);
        reserva.setStatusReserva(StatusReserva.RESERVADO);
        reserva.setEspacoComum(espacoComum);

        reservaRepository.save(reserva);

        val quantidadeDeReservasAtivas = reservaRepository.quantasReservasAtivasExistemParaOMorador(morador);
        assertThat(quantidadeDeReservasAtivas).isEqualTo(1L);

    }

    @Test
    void devePesquisarTodasAsReservasParaUmEspacoComumNoMes() {

        Condominio condominio = getApartamento().getBloco().getCondominio();
        Morador morador =getMorador();
        condominioRepository.save(condominio);

        EspacoComum espacoComum = new EspacoComum();
        espacoComum.setNome("EspacoComum");
        espacoComum.setDescricao("Piscina");
        espacoComum.setCapacidadeMaxima(100);
        espacoComum.setCondominio(condominio);

        espacoComumRepository.save(espacoComum);
        LocalDate dataReserva = LocalDate.now();

        Reserva reserva = new Reserva();
        reserva.setDataReserva(dataReserva);
        reserva.setMorador(morador);
        reserva.setStatusReserva(StatusReserva.RESERVADO);
        reserva.setEspacoComum(espacoComum);

        reservaRepository.save(reserva);

        val inicioDoMes = dataReserva.with(TemporalAdjusters.firstDayOfMonth());
        val fimDoMes = dataReserva.with(TemporalAdjusters.lastDayOfMonth());

        val founded = reservaRepository.findByEspacoComum(espacoComum, inicioDoMes, fimDoMes);

        assertThat(founded.isEmpty()).isFalse();


    }


}
