package com.pmrodrigues.condominio.dto;

import java.util.Date;
import java.util.List;

public record MoradorRequestDTO(String moradorId,
                                String email,
                                String apartamento,
                                String bloco,
                                String nome,
                                String cpf,
                                Date dataNascimento,
                                List<TelefoneDTO> telefones) {


}
