package com.pmrodrigues.condominio.controller;

import com.pmrodrigues.condominio.dto.ApartamentoDTO;
import com.pmrodrigues.condominio.dto.VisitanteRequestDTO;
import com.pmrodrigues.condominio.dto.VisitanteResponseDTO;
import com.pmrodrigues.condominio.services.VisitanteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/visitantes")
@RestController
@Slf4j
@RequiredArgsConstructor
public class VisitanteController {

    private final VisitanteService visitanteService;

    @PostMapping()
    public ResponseEntity<VisitanteResponseDTO> informarEntradaDeVisitante(@RequestBody VisitanteRequestDTO visitante) {
        log.info("registrando a entrada do visitante {}", visitante);

        val response = visitanteService.informarEntradaDeVisitante(visitante);
        log.info("reserva {} efetuado com sucesso", response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<VisitanteResponseDTO>> recuperarVisitantesPorApartamento(@RequestParam("apartamento") @NonNull String apartamento){
        log.info("pesquisando todas as visitas para o apartamento {}", apartamento);

        List<VisitanteResponseDTO> visitantes = visitanteService.recuperarVisitantesPorApartamento(new ApartamentoDTO(apartamento, null, null ,null ));

        log.info("visitas encontradas {}", visitantes);

        return ResponseEntity.status(HttpStatus.OK).body(visitantes);

    }
}
