package test.com.pmrodrigues.condominio.repositories;


import com.pmrodrigues.condominio.models.*;
import com.pmrodrigues.condominio.repositories.CondominioRepository;
import com.pmrodrigues.condominio.repositories.MoradorRepository;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import com.pmrodrigues.condominio.repositories.specifications.SpecificationMorador;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


import static com.pmrodrigues.condominio.repositories.specifications.SpecificationMorador.cpf;
import static com.pmrodrigues.condominio.repositories.specifications.SpecificationMorador.nome;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;
import static test.com.pmrodrigues.condominio.utils.GeradorCPF.gerarCPF;

@DataJpaTest
@ContextConfiguration(classes = UsuarioRepository.class)
@EnableJpaRepositories(basePackages = {"com.pmrodrigues.condominio.repositories"})
@EntityScan("com.pmrodrigues.condominio.models")
public class TestMoradorRepository {

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private CondominioRepository condominioRepository;


    private Morador getMorador() {

        Morador morador = Morador.builder()
                .nome("João da Silva")
                .dataNascimento(Date.from(
                        LocalDate.of(1990, 1, 1)
                                .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .email("joao.silva@example.com")
                .cpf(gerarCPF())
                .username("username")
                .password("password")
                .apartamento(this.getApartamento())
                .build();

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
    void testFindByCpf() {
        val saved = this.getMorador();
        Optional<Morador> morador = moradorRepository.findByCpf(saved.getCpf());
        assertThat(morador).isPresent();
        assertThat(morador.get().getNome()).isEqualTo("João da Silva");
    }

    @Test
    void testFindByEmail() {
        this.getMorador();
        Optional<Morador> morador = moradorRepository.findByEmail("joao.silva@example.com");
        assertThat(morador).isPresent();
        assertThat(morador.get().getNome()).isEqualTo("João da Silva");
    }

    @Test
    void testFindByGuid() {

        Morador morador = new Morador();
        morador.setNome("Maria Oliveira");
        morador.setDataNascimento(Date.from(
                LocalDate.of(1985, 5, 15)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        morador.setEmail("maria.oliveira@example.com");
        morador.setCpf(gerarCPF());
        morador.setUsername("username2");
        morador.setPassword("password");
        morador.setApartamento(this.getApartamento());
        moradorRepository.save(morador);

        Optional<Morador> foundMorador = moradorRepository.findByGuid(morador.getGuid());
        assertThat(foundMorador).isPresent();
        assertThat(foundMorador.get().getNome()).isEqualTo("Maria Oliveira");
    }

    @Test
    void testAddTelefone() {

        Telefone telefone = new Telefone();
        telefone.setNumero("1234-5678");

        // Criar e salvar um morador
        Morador morador = new Morador();
        morador.setNome("João da Silva");
        morador.setDataNascimento(Date.from(
                LocalDate.of(1990, 1, 1)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        morador.setEmail("joao.silva_2@example.com");
        morador.setCpf(gerarCPF());
        morador.setUsername("username_2");
        morador.setPassword("password");
        morador.setApartamento(this.getApartamento());

        moradorRepository.save(morador);

        morador.adicionarTelefone(List.of(telefone));
        moradorRepository.save(morador);

        // Verificar se o telefone foi adicionado
        Morador foundMorador = moradorRepository.findById(morador.getId()).orElse(null);
        assertThat(foundMorador).isNotNull();
        assertThat(foundMorador.getTelefones()).hasSize(1);
        assertThat(foundMorador.getTelefones().iterator().next().getNumero()).isEqualTo("1234-5678");
    }

    @Test
    void testRemoveTelefone() {
        // Criar e adicionar dois telefones ao morador
        Telefone telefone1 = new Telefone();
        telefone1.setNumero("1234-5678");

        Telefone telefone2 = new Telefone();
        telefone2.setNumero("8765-4321");

        // Criar e salvar um morador
        Morador morador = new Morador();
        morador.setNome("João da Silva");
        morador.setDataNascimento(Date.from(
                LocalDate.of(1990, 1, 1)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        morador.setEmail("joao.silva_2@example.com");
        morador.setCpf(gerarCPF());
        morador.setUsername("username_3");
        morador.setPassword("password");
        morador.setApartamento(this.getApartamento());

        moradorRepository.save(morador);

        morador.adicionarTelefone(List.of(telefone1, telefone2));
        moradorRepository.save(morador);

        // Remover um telefone
        morador.getTelefones().remove(telefone1);
        moradorRepository.save(morador);

        // Verificar se o telefone foi removido
        Morador foundMorador = moradorRepository.findById(morador.getId()).orElse(null);
        assertThat(foundMorador).isNotNull();
        assertThat(foundMorador.getTelefones()).hasSize(1);
        assertThat(foundMorador.getTelefones().iterator().next().getNumero()).isEqualTo("8765-4321");
    }

    @Test
    void devePesquisarMoradoresPorQualquerParametro() {

        var moradores = new ArrayList<Morador>();
        var apartamento = getApartamento();

        moradores.add( Morador.builder()
                .nome("João Silva")
                .dataNascimento(Date.from(
                        LocalDate.of(1985, 4, 15)
                                .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .email("joao.silva@example.com")
                .cpf(gerarCPF())
                .username("joao.silva@example.com")
                .apartamento(apartamento)
                .build());

        moradores.add( Morador.builder()
                .nome("Maria Souza")
                .dataNascimento(Date.from(
                        LocalDate.of(1990, 6, 20)
                                .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .email("maria.souza@example.com")
                .cpf(gerarCPF())
                .username("maria.souza@example.com")
                .apartamento(apartamento)
                .build());

        moradores.add( Morador.builder()
                .nome("Carlos Alves")
                .dataNascimento(Date.from(
                        LocalDate.of(1982, 12, 8)
                                .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .email("carlos.alves@example.com")
                .cpf(gerarCPF())
                .username("carlos.alves@example.com")
                .apartamento(apartamento)
                .build());

        moradorRepository.saveAll(moradores);

        int randomIndex = ThreadLocalRandom.current().nextInt(moradores.size());

        List<Morador> searched = moradorRepository.findAll(where(cpf(moradores.get(randomIndex).getCpf())));
        assertThat(searched.isEmpty()).isFalse();
        assertThat(searched.size()).isEqualTo(1);

        searched = moradorRepository.findAll(where(nome(moradores.get(randomIndex).getNome())));
        assertThat(searched.isEmpty()).isFalse();
        assertThat(searched.size()).isEqualTo(1);


    }
}