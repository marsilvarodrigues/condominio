package com.pmrodrigues.condominio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Tentativa de alterar/cancelar/reservar locação do espaço comum fora do periodo permitido")
public class ForaDoPrazoParaReservarException extends RuntimeException {
}
