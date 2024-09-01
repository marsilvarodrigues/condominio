package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Morador;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

public record MoradorResponseDTO(String guid, String nome, LocalDate dataNascimento, String email, String cpf, ApartamentoDTO apartamentoDTO) {

    public static MoradorResponseDTO fromMorador(@NonNull Morador morador) {
        return new MoradorResponseDTO(morador.getGuid(),
                morador.getNome(),
                morador.getDataNascimento(),
                morador.getEmail(),
                morador.getCpf(),
                ApartamentoDTO.fromApartamento(morador.getApartamento()));
    }
}
