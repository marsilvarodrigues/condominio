package com.pmrodrigues.condominio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Alteração não permitida")
public class AlteracaoNaoPermitadaException extends RuntimeException{
}
