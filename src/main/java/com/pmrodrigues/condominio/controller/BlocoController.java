package com.pmrodrigues.condominio.controller;

import com.pmrodrigues.condominio.dto.BlocoDTO;
import com.pmrodrigues.condominio.dto.PerfilDTO;
import com.pmrodrigues.condominio.repositories.BlocoRepository;
import com.pmrodrigues.condominio.repositories.PerfilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/blocos")
@RestController
@Slf4j
@RequiredArgsConstructor
public class BlocoController {

    private final BlocoRepository blocoRepository;

    @GetMapping
    public ResponseEntity<List<BlocoDTO>> listarBlocos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(blocoRepository.findAll()
                        .stream()
                        .map(BlocoDTO::fromBloco)
                        .collect(Collectors.toList()));
    }
}
