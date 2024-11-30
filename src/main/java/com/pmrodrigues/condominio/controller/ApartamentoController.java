package com.pmrodrigues.condominio.controller;

import com.pmrodrigues.condominio.dto.ApartamentoDTO;
import com.pmrodrigues.condominio.dto.BlocoDTO;
import com.pmrodrigues.condominio.repositories.ApartamentoRepository;
import com.pmrodrigues.condominio.repositories.BlocoRepository;
import com.pmrodrigues.condominio.services.ApartamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/apartamentos")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ApartamentoController {

    private final ApartamentoService apartamentoService;

    @GetMapping("/{blocoId}/{apartamento}")
    public ResponseEntity<List<ApartamentoDTO>> listarApartamentos(@PathVariable String blocoId, @PathVariable String apartamento) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apartamentoService.listApartamentosPorBloco(blocoId, apartamento));
    }

    @GetMapping("/{blocoId}")
    public ResponseEntity<List<ApartamentoDTO>> listarApartamentos(@PathVariable String blocoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apartamentoService.listApartamentosPorBloco(blocoId));
    }
}
