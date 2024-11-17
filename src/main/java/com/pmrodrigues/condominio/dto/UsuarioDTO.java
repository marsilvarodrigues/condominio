package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Perfil;
import com.pmrodrigues.condominio.models.Usuario;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public record UsuarioDTO(Long id, String usuarioId, String username, Date dataCriacao, List<PerfilDTO> perfis) {
    public static UsuarioDTO fromUsuario(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(),
                usuario.getGuid(),
                usuario.getUsername(),
                usuario.getCreatedDate(),
                usuario.getPerfis().stream().map(PerfilDTO::fromPerfil).sorted(Comparator.comparing(PerfilDTO::nome)).toList()
                );
    }
}
