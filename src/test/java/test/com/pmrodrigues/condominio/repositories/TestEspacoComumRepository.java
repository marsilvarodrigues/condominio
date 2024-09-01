package test.com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.*;
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
public class TestEspacoComumRepository {

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private EspacoComumRepository espacoComumRepository;

    @Test
    void testFindByGuid() {

        Condominio condominio = new Condominio();
        condominio.setNome("Condomínio Jardim das Flores");
        condominio.setQuantidadeBlocos(5L);

        condominioRepository.save(condominio);

        EspacoComum espacoComum = new EspacoComum();
        espacoComum.setNome("EspacoComum");
        espacoComum.setDescricao("Piscina");
        espacoComum.setCapacidadeMaxima(100);
        espacoComum.setCondominio(condominio);

        espacoComumRepository.save(espacoComum);

        String guid = espacoComum.getGuid();

        // Testar a busca por GUID
        Optional<EspacoComum> found = espacoComumRepository.findByGuid(guid);

        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo("EspacoComum");

    }

    @Test
    void testFindByGuidNotFound() {
        // Testar a busca por GUID inexistente
        Optional<EspacoComum> found = espacoComumRepository.findByGuid("non-existing-guid");
        assertThat(found).isNotPresent();
    }


}
