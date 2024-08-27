package test.com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Bloco;
import com.pmrodrigues.condominio.models.Condominio;
import com.pmrodrigues.condominio.repositories.BlocoRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = UsuarioRepository.class)
@EnableJpaRepositories(basePackages = {"com.pmrodrigues.condominio.repositories"})
@EntityScan("com.pmrodrigues.condominio.models")
public class TestBlocoRepository {

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private CondominioRepository condominioRepository;


    @Test
    void testFindByGuid() {

        Condominio condominio = new Condominio();
        condominio.setNome("Condomínio Jardim das Flores");
        condominio.setQuantidadeBlocos(5L);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco A");
        bloco.setNumero("101");
        bloco.setQuantidadeApartamento(10L);
        condominio.adicionarBloco(bloco);

        condominio = condominioRepository.save(condominio);

        String guid = condominio.getBlocos().stream().findFirst().get().getGuid();

        // Testar a busca por GUID
        Optional<Bloco> found = blocoRepository.findByGuid(guid);
        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo("Bloco A");
        assertThat(found.get().getNumero()).isEqualTo("101");
    }

    @Test
    void testFindByGuidNotFound() {
        // Testar a busca por GUID inexistente
        Optional<Bloco> found = blocoRepository.findByGuid("non-existing-guid");
        assertThat(found).isNotPresent();
    }

    @Test
    void testSaveBloco() {
        Condominio condominio = new Condominio();
        condominio.setNome("Condomínio Jardim das Flores");
        condominio.setQuantidadeBlocos(5L);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco A");
        bloco.setNumero("101");
        bloco.setQuantidadeApartamento(10L);
        condominio.adicionarBloco(bloco);

        condominioRepository.save(condominio);
        bloco.setNome("Bloco B");
        blocoRepository.save(bloco);

        String guid = bloco.getGuid();
        Bloco savedBloco = blocoRepository.findByGuid(guid).get();

        assertThat(savedBloco).isNotNull();
        assertThat(savedBloco.getId()).isNotNull();
        assertThat(savedBloco.getNome()).isEqualTo("Bloco B");
    }

    @Test
    void testDeleteBloco() {
        // Testar a exclusão de um bloco
        Condominio condominio = new Condominio();
        condominio.setNome("Condomínio Jardim das Flores");
        condominio.setQuantidadeBlocos(5L);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco A");
        bloco.setNumero("101");
        bloco.setQuantidadeApartamento(10L);
        condominio.adicionarBloco(bloco);

        condominioRepository.save(condominio);

        String guid = bloco.getGuid();
        Bloco savedBloco = blocoRepository.findByGuid(guid).get();

        // Verificar a existência antes da exclusão
        assertThat(blocoRepository.findById(savedBloco.getId())).isPresent();

        // Excluir o bloco
        blocoRepository.deleteById(savedBloco.getId());

        // Verificar a ausência após a exclusão
        assertThat(blocoRepository.findById(savedBloco.getId())).isNotPresent();
    }
}
