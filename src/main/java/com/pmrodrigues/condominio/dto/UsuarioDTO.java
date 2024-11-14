package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Perfil;
import com.pmrodrigues.condominio.models.Usuario;

import java.time.LocalDate;
import java.util.List;

public record UsuarioDTO(String usuarioId, String username, LocalDate dataCriacao, List<String> perfis) {
    public static UsuarioDTO fromUsuario(Usuario usuario) {
        return new UsuarioDTO(usuario.getGuid(),
                usuario.getUsername(),
                LocalDate.from(usuario.getCreatedDate().toInstant()),
                usuario.getPerfis().stream().map(Perfil::getAuthority).toList()
                );
    }
}
