package com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.MoradorRequestDTO;
import com.pmrodrigues.condominio.dto.MoradorResponseDTO;
import com.pmrodrigues.condominio.dto.TelefoneDTO;
import com.pmrodrigues.condominio.exceptions.ApartamentoNotFoundException;
import com.pmrodrigues.condominio.exceptions.BlocoNotFoundException;
import com.pmrodrigues.condominio.exceptions.MoradorNotFoundException;
import com.pmrodrigues.condominio.exceptions.NotFoundException;
import com.pmrodrigues.condominio.models.Morador;
import com.pmrodrigues.condominio.repositories.ApartamentoRepository;
import com.pmrodrigues.condominio.repositories.BlocoRepository;
import com.pmrodrigues.condominio.repositories.MoradorRepository;
import com.pmrodrigues.condominio.repositories.TelefoneRepository;
import com.pmrodrigues.condominio.utilities.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pmrodrigues.condominio.repositories.specifications.SpecificationMorador.*;
import static java.lang.String.format;
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
    private final BlocoRepository blocoRepository;


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
                .telefones(Optional.ofNullable(dto.telefones())
                        .stream() // Cria um stream a partir do Optional
                        .flatMap(Collection::stream) // Mapeia o conteúdo do Optional para um stream se presente
                        .map(TelefoneDTO::toTelefone)
                        .collect(Collectors.toSet()))
                .build();

        val saved = moradorRepository.save(morador);

        log.debug("morador {} salvo, preparando email de cadastro", saved);

        emailService.subject(format("Seja bem-vindo ao condomino %s", apartamento.getBloco().getCondominio().getNome()))
                .from("condominio@condominio.com")
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

    public MoradorResponseDTO atualizarDadosMorador(MoradorRequestDTO dto) {

        log.info("atualizando os dados do morador {}", dto);

        val apartamento = apartamentoRepository.findByGuid(dto.apartamento())
                .orElseThrow(ApartamentoNotFoundException::new);

        val morador = moradorRepository.findByGuid(dto.moradorId())
                .orElseThrow(MoradorNotFoundException::new);

        morador.setCpf(dto.cpf());
        morador.setEmail(dto.email());
        morador.setNome(dto.nome());
        morador.setDataNascimento(dto.dataNascimento());
        morador.setApartamento(apartamento);

        morador.adicionarTelefone(Optional.ofNullable(dto.telefones())
                                          .orElse(Collections.emptyList())
                                          .stream()
                                          .map(TelefoneDTO::toTelefone)
                                          .toList());

        moradorRepository.save(morador);
        log.info("morador {} salvo com sucesso", morador);
        return MoradorResponseDTO.fromMorador(morador);

    }

    public List<MoradorResponseDTO> pesquisarMorador(MoradorRequestDTO dto) {
        log.info("pesquisando o morador com os parametros {}", dto);


        val apartamento = Optional.ofNullable(dto.apartamento())
                    .map( (a) -> apartamentoRepository.findByGuid(dto.apartamento())
                                                             .orElseThrow(ApartamentoNotFoundException::new))
                    .orElse(null);

        val bloco = Optional.ofNullable(dto.bloco())
                .map( (a) -> blocoRepository.findByGuid(dto.bloco())
                        .orElseThrow(BlocoNotFoundException::new))
                .orElse(null);

        return moradorRepository.findAll(where(nome(dto.nome())
                .and(bloco(bloco))
                .and(apartamento(apartamento))))
                .stream().map(MoradorResponseDTO::fromMorador)
                .toList();
    }

    public MoradorResponseDTO recuperarMorador(String guid) {
        val morador = moradorRepository.findByGuid(guid).orElseThrow(MoradorNotFoundException::new);

        return MoradorResponseDTO.fromMorador(morador);
    }

    public void excluirMorador(String guid) {
        val morador = moradorRepository.findByGuid(guid).orElseThrow(MoradorNotFoundException::new);

        moradorRepository.delete(morador);
    }
}
