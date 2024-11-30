package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Telefone;

public record TelefoneDTO(String telefoneId, String numero) {
    public static Telefone toTelefone(TelefoneDTO dto) {
        return Telefone.builder()
                .numero(dto.numero())
                .guid(dto.telefoneId())
                .build();
    }
}
