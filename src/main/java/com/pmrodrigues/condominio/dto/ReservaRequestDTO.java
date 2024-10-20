package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.enums.StatusReserva;

import java.time.LocalDate;
import java.util.Date;

public record ReservaRequestDTO(String reservaId, String espacaoComum, String morador, Date dataReserva,
                                StatusReserva statusReserva) {
}
