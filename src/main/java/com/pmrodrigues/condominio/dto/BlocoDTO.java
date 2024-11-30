package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Bloco;

public record BlocoDTO(String blocoId, String nome) {
    public static BlocoDTO fromBloco(Bloco bloco) {
        return new BlocoDTO(bloco.getGuid(), bloco.getNome());
    }
}
