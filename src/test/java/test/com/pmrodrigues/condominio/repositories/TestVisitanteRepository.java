package test.com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Apartamento;
import com.pmrodrigues.condominio.models.Bloco;
import com.pmrodrigues.condominio.models.Condominio;
import com.pmrodrigues.condominio.models.Visitante;
import com.pmrodrigues.condominio.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

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

    @Test
    void testFindByGuid() {

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

        Visitante visitante = new Visitante();
        visitante.setNome("João da Silva");
        visitante.setApartamento(apartamento);

        visitanteRepository.save(visitante);


        String guid = visitante.getGuid();

        // Testar a busca por GUID
        Optional<Visitante> found = visitanteRepository.findByGuid(guid);

        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo("João da Silva");

    }

    @Test
    void testFindByGuidNotFound() {
        // Testar a busca por GUID inexistente
        Optional<Apartamento> found = apartamentoRepository.findByGuid("non-existing-guid");
        assertThat(found).isNotPresent();
    }


}
