package com.pmrodrigues.condominio.dto;


import com.pmrodrigues.condominio.models.EspacoComum;
import lombok.NonNull;

public record EspacoComumResponseDTO(
        String guid,
        String nome,
        String descricao,
        Integer capacidadeMaxima) {

    public static EspacoComumResponseDTO fromEspacoComum(@NonNull EspacoComum espacoComum) {
        return new EspacoComumResponseDTO(espacoComum.getGuid(), espacoComum.getNome(), espacoComum.getDescricao(), espacoComum.getCapacidadeMaxima());
    }
}
