package com.pmrodrigues.condominio.dto;


import com.pmrodrigues.condominio.models.EspacoComum;
import lombok.NonNull;

public record EspacoComumDTO(
        String guid,
        String nome,
        String descricao,
        Integer capacidadeMaxima) {

    public static EspacoComumDTO fromEspacoComum(@NonNull EspacoComum espacoComum) {
        return new EspacoComumDTO(espacoComum.getGuid(), espacoComum.getNome(), espacoComum.getDescricao(), espacoComum.getCapacidadeMaxima());
    }
}