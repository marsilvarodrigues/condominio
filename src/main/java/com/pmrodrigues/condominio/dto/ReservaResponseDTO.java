package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.enums.StatusReserva;
import com.pmrodrigues.condominio.models.Reserva;
import lombok.NonNull;

import java.time.LocalDate;

public record ReservaResponseDTO(String reservaId, EspacoComumResponseDTO espacaoComum, MoradorResponseDTO morador, LocalDate dataReserva,
                                 StatusReserva statusReserva) {

    public static ReservaResponseDTO fromReserva(@NonNull Reserva reserva) {
        return new ReservaResponseDTO(reserva.getGuid(),
                EspacoComumResponseDTO.fromEspacoComum(reserva.getEspacoComum()),
                MoradorResponseDTO.fromMorador(reserva.getMorador()),
                reserva.getDataReserva(),
                reserva.getStatusReserva());
    }
}
