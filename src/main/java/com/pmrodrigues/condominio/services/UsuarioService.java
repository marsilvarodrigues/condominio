package com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.UsuarioDTO;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.pmrodrigues.condominio.repositories.specifications.SpecificationUsuario.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> pesquisarUsuarios(UsuarioDTO usuarioDTO) {

        log.info("iniciando pesquisa de usuarios com os parametros {}", usuarioDTO);

        return this.usuarioRepository.findAll(
                where(usuario(usuarioDTO.username()))
                .and(perfis(usuarioDTO.perfis()))
        ).stream().map(UsuarioDTO::fromUsuario)
                .collect(Collectors.toList());

    }
}
