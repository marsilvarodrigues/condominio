package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.enums.StatusReserva;

import java.time.LocalDate;

public record ReservaRequestDTO(String reservaId, String espacaoComum, String morador, LocalDate dataReserva,
                                StatusReserva statusReserva) {
}
