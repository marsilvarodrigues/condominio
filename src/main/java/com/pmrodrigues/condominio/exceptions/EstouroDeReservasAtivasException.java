package com.pmrodrigues.condominio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Estouro na quantidade de reservas permitidas para o morador")
public class EstouroDeReservasAtivasException extends RuntimeException{
}
