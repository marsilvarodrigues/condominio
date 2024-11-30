package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Perfil;

public record PerfilDTO(Long id, String perfilId, String nome) {
    public static PerfilDTO fromPerfil(Perfil perfil) {
        return new PerfilDTO(perfil.getId(), perfil.getGuid(), perfil.getAuthority());
    }
}
