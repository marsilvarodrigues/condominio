package com.pmrodrigues.condominio.controller;

import com.pmrodrigues.condominio.dto.PerfilDTO;
import com.pmrodrigues.condominio.models.Perfil;
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

@RequestMapping("/perfis")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilRepository perfilRepository;

    @GetMapping
    public ResponseEntity<List<PerfilDTO>> listarPerfis() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(perfilRepository.findAll()
                        .stream()
                        .map(PerfilDTO::fromPerfil)
                        .collect(Collectors.toList()));
    }
}
