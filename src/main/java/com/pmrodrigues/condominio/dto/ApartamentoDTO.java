package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Apartamento;
import lombok.NonNull;

public record ApartamentoDTO(String apartamentoId, String blocoId, String bloco, String apartamento) {
    public static ApartamentoDTO fromApartamento(Apartamento apartamento) {
        if (apartamento != null ) {
            return new ApartamentoDTO(apartamento.getGuid(), apartamento.getBloco().getGuid(), apartamento.getBloco().getNome(), apartamento.getNumero());
        }
        return null;
    }
}
