package test.com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.CondominioApplication;
import com.pmrodrigues.condominio.models.Perfil;
import com.pmrodrigues.condominio.models.Usuario;
import com.pmrodrigues.condominio.repositories.PerfilRepository;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.pmrodrigues.condominio.repositories.specifications.SpecificationUsuario.perfis;
import static com.pmrodrigues.condominio.repositories.specifications.SpecificationUsuario.usuario;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ContextConfiguration(classes = UsuarioRepository.class)
@EnableJpaRepositories(basePackages = {"com.pmrodrigues.condominio.repositories"})
@EntityScan("com.pmrodrigues.condominio.models")
public class TestUsuarioRepository {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

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

    @Test
    void testPesquisarUsuariosComParametros() {

        IntStream.range(1, 10).forEach( i -> {
            val usuario = Usuario.builder().perfis(Set.of(perfilRepository
                    .findById(RandomUtils.secure().randomLong(1, 4)))
                    .stream().flatMap(Optional::stream).collect(Collectors.toSet())
            )
                    .username("usuario_" + i)
                    .password(RandomStringUtils.secure().nextAlphabetic(8))
                    .build();

            usuarioRepository.save(usuario);
        });

        var founded = !usuarioRepository.findAll(Specification.where(usuario("usuario_1"))).isEmpty();
        assertThat(founded).isTrue();


        founded = !usuarioRepository.findAll(Specification.where(perfis(
                perfilRepository.findAll()
                        .stream()
                        .map(Perfil::getAuthority)
                        .toList()))).isEmpty();
        assertThat(founded).isTrue();


    }
}

