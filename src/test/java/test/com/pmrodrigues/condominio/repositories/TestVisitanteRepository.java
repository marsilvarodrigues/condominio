package test.com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.*;
import com.pmrodrigues.condominio.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.val;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = UsuarioRepository.class)
@EnableJpaRepositories(basePackages = {"com.pmrodrigues.condominio.repositories"})
@EntityScan("com.pmrodrigues.condominio.models")
public class TestVisitanteRepository {

    @Autowired
    private ApartamentoRepository apartamentoRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private VisitanteRepository visitanteRepository;

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Morador getMorador() {

        Morador morador = new Morador();
        morador.setNome("João da Silva");
        morador.setDataNascimento(LocalDate.of(1990, 1, 1));
        morador.setEmail("joao.silva@example.com");
        morador.setCpf("12345678900");
        morador.setUsername("morador");
        morador.setPassword("password");
        morador.setApartamento(this.getApartamento());

        return moradorRepository.save(morador);
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

    private Usuario getUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername(format("porteiro %s", RandomUtils.nextInt(1,100000)));
        usuario.setPassword("123456");
        return usuarioRepository.save(usuario);
    }

    @Test
    void testFindByGuid() {

        val morador = this.getMorador();

        Visitante visitante = new Visitante();
        visitante.setNome("João da Silva");
        visitante.setApartamento(morador.getApartamento());
        visitante.setAutorizadoPor(morador);
        visitante.setRegistradoPor(this.getUsuario());

        visitanteRepository.save(visitante);

        String guid = visitante.getGuid();

        // Testar a busca por GUID
        Optional<Visitante> found = visitanteRepository.findByGuid(guid);

        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo("João da Silva");
        assertThat(found.get().getRegistradoPor()).isEqualTo(visitante.getRegistradoPor());
        assertThat(found.get().getAutorizadoPor()).isEqualTo(visitante.getAutorizadoPor());

    }

    @Test
    void testFindByGuidNotFound() {
        // Testar a busca por GUID inexistente
        Optional<Apartamento> found = apartamentoRepository.findByGuid("non-existing-guid");
        assertThat(found).isNotPresent();
    }

    @Test
    void testAddAVeiculoDoVisitante() {
        val morador = this.getMorador();

        Visitante visitante = new Visitante();
        visitante.setNome("João da Silva");
        visitante.setApartamento(morador.getApartamento());
        visitante.setAutorizadoPor(morador);
        visitante.setRegistradoPor(this.getUsuario());

        Veiculo veiculo = new Veiculo();
        veiculo.setCor("BRANCO");
        veiculo.setModelo("FIT");
        veiculo.setPlaca("LRH6605");
        veiculo.setFabricante("Honda");
        visitante.adicionaVeiculo(veiculo);

        visitanteRepository.save(visitante);

        String guid = visitante.getGuid();

        // Testar a busca por GUID
        Optional<Visitante> found = visitanteRepository.findByGuid(guid);
        assertThat(found.get().getVeiculo()).isNotNull();
        assertThat(found.get().getVeiculo().getCor()).isNotNull();
        assertThat(found.get().getVeiculo().getPlaca()).isNotNull();
        assertThat(found.get().getVeiculo().getModelo()).isNotNull();
        assertThat(found.get().getVeiculo().getFabricante()).isNotNull();

    }

    @Test
    void testDevePesquisarVisitasPorApartamento() {

        val morador = this.getMorador();
        IntStream.range(1, 11)
                .forEach( i -> {
                        Visitante visitante = new Visitante();
                        visitante.setNome(format("João %s", i));
                        visitante.setApartamento(morador.getApartamento());
                        visitante.setAutorizadoPor(morador);
                        visitante.setRegistradoPor(this.getUsuario());

                        Veiculo veiculo = new Veiculo();
                        veiculo.setCor("BRANCO");
                        veiculo.setModelo("FIT");
                        veiculo.setPlaca("LRH6605");
                        veiculo.setFabricante("Honda");
                        visitante.adicionaVeiculo(veiculo);

                        visitanteRepository.save(visitante);
                });

        List<Visitante> visitantes = visitanteRepository.findByApartamento(morador.getApartamento());
        assertThat(visitantes.isEmpty()).isFalse();
        assertThat(visitantes.size()).isEqualTo(10);

    }


}
