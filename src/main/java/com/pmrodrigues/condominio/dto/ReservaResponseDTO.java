package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.enums.StatusReserva;
import com.pmrodrigues.condominio.models.Reserva;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Date;

public record ReservaResponseDTO(String reservaId, EspacoComumDTO espacaoComum, MoradorResponseDTO morador, Date dataReserva,
                                 StatusReserva statusReserva) {

    public static ReservaResponseDTO fromReserva(@NonNull Reserva reserva) {
        return new ReservaResponseDTO(reserva.getGuid(),
                EspacoComumDTO.fromEspacoComum(reserva.getEspacoComum()),
                MoradorResponseDTO.fromMorador(reserva.getMorador()),
                reserva.getDataReserva(),
                reserva.getStatusReserva());
    }
}
