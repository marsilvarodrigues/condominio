package com.pmrodrigues.condominio.dto;

import java.time.LocalDateTime;

public record VisitanteRequestDTO(String visitaId, LocalDateTime dataDaVisita, String nomeDoVisitante, VeiculoDTO veiculo, String apartamento,
                                 String registradoPor, String autorizadoPor) {

}
