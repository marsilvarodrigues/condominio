package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Morador;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Date;

public record MoradorResponseDTO(Long id, String guid, String nome, Date dataNascimento, String email, String cpf, ApartamentoDTO apartamento) {

    public static MoradorResponseDTO fromMorador(@NonNull Morador morador) {
        return new MoradorResponseDTO(
                morador.getId(),
                morador.getGuid(),
                morador.getNome(),
                morador.getDataNascimento(),
                morador.getEmail(),
                morador.getCpf(),
                ApartamentoDTO.fromApartamento(morador.getApartamento()));
    }
}
