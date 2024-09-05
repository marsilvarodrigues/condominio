package com.pmrodrigues.condominio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Espaço comum não encontrador")
public class EspacoComumNotFoundException extends NotFoundException{
}
