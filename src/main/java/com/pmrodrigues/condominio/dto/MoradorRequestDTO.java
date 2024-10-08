package com.pmrodrigues.condominio.dto;

import java.time.LocalDate;
import java.util.List;

public record MoradorRequestDTO(String moradorId,
                                String email,
                                String apartamento,
                                String nome,
                                String cpf,
                                LocalDate dataNascimento,
                                List<TelefoneDTO> telefones) {


}
