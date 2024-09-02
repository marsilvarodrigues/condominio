package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Usuario;

public record UsuarioDTO(String usuarioId, String username) {
    public static UsuarioDTO fromUsuario(Usuario usuario) {
        return new UsuarioDTO(usuario.getGuid(), usuario.getUsername());
    }
}
