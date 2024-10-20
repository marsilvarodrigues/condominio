package com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.MoradorRequestDTO;
import com.pmrodrigues.condominio.dto.MoradorResponseDTO;
import com.pmrodrigues.condominio.dto.TelefoneDTO;
import com.pmrodrigues.condominio.exceptions.ApartamentoNotFoundException;
import com.pmrodrigues.condominio.exceptions.MoradorNotFoundException;
import com.pmrodrigues.condominio.exceptions.NotFoundException;
import com.pmrodrigues.condominio.models.Morador;
import com.pmrodrigues.condominio.models.Perfil;
import com.pmrodrigues.condominio.models.Telefone;
import com.pmrodrigues.condominio.repositories.ApartamentoRepository;
import com.pmrodrigues.condominio.repositories.MoradorRepository;
import com.pmrodrigues.condominio.repositories.TelefoneRepository;
import com.pmrodrigues.condominio.utilities.EmailService;
import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import javax.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.pmrodrigues.condominio.repositories.specifications.SpecificationMorador.*;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class MoradorService {

    private final MoradorRepository moradorRepository;
    private final ApartamentoRepository apartamentoRepository;
    private final EmailService emailService;
    private final TelefoneRepository telefoneRepository;


    @SneakyThrows
    public MoradorResponseDTO cadastrarNovoMorador(MoradorRequestDTO dto) {

        log.info("inserindo o morador {}", dto);

        val apartamento = apartamentoRepository.findByGuid(dto.apartamento())
                .orElseThrow(ApartamentoNotFoundException::new);

        val morador = (Morador)Morador.builder()
                .apartamento(apartamento)
                .email(dto.email())
                .cpf(dto.cpf())
                .nome(dto.nome())
                .username(dto.email())
                .dataNascimento(dto.dataNascimento())
                .telefones(dto.telefones()
                        .stream()
                        .map(TelefoneDTO::toTelefone)
                        .collect(Collectors.toSet()))
                .build();

        val saved = moradorRepository.save(morador);

        log.debug("morador {} salvo, preparando email de cadastro", saved);

        emailService.subject(format("Seja bem-vindo ao condomino %s", apartamento.getBloco().getCondominio().getNome()))
                .to(saved.getEmail())
                .text(format("Seja bem-vindo %s ao apartamento %s. Seu usuário é %s e sua senha é %s",
                        saved.getNome(),
                        saved.getApartamento().getNumero(),
                        saved.getUsername(),
                        saved.getPassword()))
                .send();

        log.debug("email enviado com sucesso");
        return MoradorResponseDTO.fromMorador(saved);
    }

    public void atualizarDadosMorador(MoradorRequestDTO dto) {

        log.info("atualizando os dados do morador {}", dto);

        val apartamento = apartamentoRepository.findByGuid(dto.apartamento())
                .orElseThrow(ApartamentoNotFoundException::new);

        val morador = moradorRepository.findByGuid(dto.moradorId())
                .orElseThrow(MoradorNotFoundException::new);

        morador.withCpf(dto.cpf())
                .withEmail(dto.email())
                .withNome(dto.nome())
                .withDataNascimento(dto.dataNascimento())
                .withApartamento(apartamento);

        morador.getTelefones().clear();

        //adiciona novos numeros
        dto.telefones()
           .stream()
           .filter( t -> t.telefoneId() == null )
           .map(TelefoneDTO::toTelefone)
           .forEach( telefone -> morador.adicionarTelefone(telefone));

        dto
           .telefones()
           .stream()
           .filter( t -> t.telefoneId() != null )
           .map( t -> {
               val telefone = telefoneRepository.findByGuid(t.telefoneId())
                       .orElseThrow(NotFoundException::new);
               telefone.withNumero(t.numero());
               return telefone;
           })
           .forEach( telefone -> morador.adicionarTelefone(telefone));

        moradorRepository.save(morador);

        log.info("morador {} salvo com sucesso", morador);

    }

    public List<MoradorResponseDTO> pesquisarMorador(MoradorRequestDTO dto) {
        log.info("pesquisando o morador com os parametros {}", dto);

        val apartamento = apartamentoRepository.findByGuid(dto.apartamento())
                .orElseThrow(ApartamentoNotFoundException::new);

        return moradorRepository.findAll(where(nome(dto.nome())
                .and(apartamento(apartamento))
                .and(bloco(apartamento.getBloco()))))
                .stream().map(MoradorResponseDTO::fromMorador)
                .toList();
    }
}
