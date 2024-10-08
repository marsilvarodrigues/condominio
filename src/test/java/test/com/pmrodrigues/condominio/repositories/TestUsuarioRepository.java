package test.com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.CondominioApplication;
import com.pmrodrigues.condominio.models.Usuario;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ContextConfiguration(classes = UsuarioRepository.class)
@EnableJpaRepositories(basePackages = {"com.pmrodrigues.condominio.repositories"})
@EntityScan("com.pmrodrigues.condominio.models")
public class TestUsuarioRepository {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testFindByGuid() {

        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        usuario.setPassword("123456");
        usuario = usuarioRepository.save(usuario);

        assertThat(usuario.getId()).isGreaterThan(0L);
        assertThat(usuario.getGuid()).isNotNull();

        val uuid = usuario.getGuid();
        val founded = usuarioRepository.findByGuid(uuid);

        assertThat(founded.get()).isEqualTo(usuario);

    }

    @Test
    void testPesquisarPorUsername() {

        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        usuario.setPassword("123456");
        usuario = usuarioRepository.save(usuario);

        assertThat(usuario.getId()).isGreaterThan(0L);
        assertThat(usuario.getGuid()).isNotNull();

        val username = usuario.getUsername();
        val founded = usuarioRepository.findByUsername(username);

        assertThat(founded.get()).isEqualTo(usuario);
    }
}

