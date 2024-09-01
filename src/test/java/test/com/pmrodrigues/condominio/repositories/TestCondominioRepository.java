package test.com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Condominio;
import com.pmrodrigues.condominio.repositories.CondominioRepository;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = UsuarioRepository.class)
@EnableJpaRepositories(basePackages = {"com.pmrodrigues.condominio.repositories"})
@EntityScan("com.pmrodrigues.condominio.models")
public class TestCondominioRepository {

    @Autowired
    private CondominioRepository condominioRepository;


    @Test
    void testFindByGuid() {
        // Testar a busca por GUID

        Condominio condominio = new Condominio();
        condominio.setNome("Condomínio Jardim das Flores");
        condominio.setQuantidadeBlocos(5L);

        condominioRepository.save(condominio);
        Optional<Condominio> found = condominioRepository.findByGuid(condominio.getGuid());
        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo("Condomínio Jardim das Flores");
    }

    @Test
    void testFindByGuidNotFound() {
        Optional<Condominio> found = condominioRepository.findByGuid("non-existing-guid");
        assertThat(found).isNotPresent();
    }
}