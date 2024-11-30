package com.pmrodrigues.condominio.dto;

import java.time.LocalDateTime;
import java.util.Date;

public record VisitanteRequestDTO(String visitaId, Date dataDaVisita, String nomeDoVisitante, VeiculoDTO veiculo, String apartamento,
                                  String registradoPor, String autorizadoPor) {

}
