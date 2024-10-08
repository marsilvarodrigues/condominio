package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Visitante;

import java.time.LocalDateTime;

public record VisitanteDTO (String visitaId, String nomeDoVisitante, LocalDateTime dataDaVisita, VeiculoDTO veiculo, ApartamentoDTO apartamento,
                            UsuarioDTO registradoPor, MoradorResponseDTO autorizadoPor) {

    public static VisitanteDTO fromVisita(Visitante visitante) {
        return new VisitanteDTO(visitante.getGuid(),
                visitante.getNome(),
                visitante.getDataVisita(),
                VeiculoDTO.fromVeiculo(visitante.getVeiculo()),
                ApartamentoDTO.fromApartamento(visitante.getApartamento()),
                UsuarioDTO.fromUsuario(visitante.getRegistradoPor()),
                MoradorResponseDTO.fromMorador(visitante.getAutorizadoPor()));
    }
}
