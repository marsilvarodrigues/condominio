package com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.NovoUsuarioDTO;
import com.pmrodrigues.condominio.dto.PerfilDTO;
import com.pmrodrigues.condominio.dto.UsuarioDTO;
import com.pmrodrigues.condominio.exceptions.UsuarioNotFoundException;
import com.pmrodrigues.condominio.models.Perfil;
import com.pmrodrigues.condominio.models.Usuario;
import com.pmrodrigues.condominio.repositories.PerfilRepository;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.pmrodrigues.condominio.repositories.specifications.SpecificationUsuario.perfis;
import static com.pmrodrigues.condominio.repositories.specifications.SpecificationUsuario.usuario;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PerfilRepository perfilRepository;

    public List<UsuarioDTO> pesquisarUsuarios(UsuarioDTO usuarioDTO) {

        log.info("iniciando pesquisa de usuarios com os parametros {}", usuarioDTO);
        List<String> perfis = Optional.ofNullable(usuarioDTO.perfis())
                .stream()
                .flatMap(Collection::stream)
                .map(PerfilDTO::nome)
                .collect(Collectors.toList());

        return this.usuarioRepository.findAll(
                where(usuario(usuarioDTO.username()))
                .and(perfis(perfis))
        ).stream().map(UsuarioDTO::fromUsuario)
                .collect(Collectors.toList());

    }

    public UsuarioDTO recuperarUsuario(String usuarioId) {
        return usuarioRepository.findByGuid(usuarioId).map(UsuarioDTO::fromUsuario)
                .orElseThrow(UsuarioNotFoundException::new);
    }

    public UsuarioDTO alterarUsuario(UsuarioDTO usuarioDTO) {
        log.info("alterando usuario {}", usuarioDTO);

        List<Perfil> perfis = perfilRepository.findAllByAuthorityIn(Optional.ofNullable(usuarioDTO.perfis())
                .orElse(Collections.emptyList()) // Retorna uma lista vazia se perfis() for null
                .stream()
                .map(PerfilDTO::nome).toList());
        Set<Perfil> roles = new HashSet<>(perfis);

        val usuario = usuarioRepository.findByGuid(usuarioDTO.usuarioId())
                .orElseThrow(UsuarioNotFoundException::new);

        usuario.setUsername(usuarioDTO.username());
        usuario.adicionarPerfil(roles);

        val saved = usuarioRepository.save(usuario);

        return UsuarioDTO.fromUsuario(saved);


    }

    public UsuarioDTO criarNovoUsuario(NovoUsuarioDTO usuarioDTO) {
        log.info("alterando usuario {}", usuarioDTO);

        List<String> perfis = Optional.ofNullable(usuarioDTO.perfis())
                .orElse(Collections.emptyList()) // Retorna uma lista vazia se perfis() for null
                .stream()
                .map(PerfilDTO::nome).toList();
        Set<Perfil> roles = new HashSet<>(perfilRepository.findAllByAuthorityIn(perfis));


        val usuario = Usuario.builder()
                .username(usuarioDTO.username())
                .perfis(roles)
                .password(usuarioDTO.password())
                .build();
        val saved = usuarioRepository.save(usuario);

        return UsuarioDTO.fromUsuario(saved);
    }
}
