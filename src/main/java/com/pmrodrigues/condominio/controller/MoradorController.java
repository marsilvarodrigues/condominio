package com.pmrodrigues.condominio.controller;

import com.pmrodrigues.condominio.dto.MoradorRequestDTO;
import com.pmrodrigues.condominio.dto.MoradorResponseDTO;
import com.pmrodrigues.condominio.models.Morador;
import com.pmrodrigues.condominio.services.MoradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moradores")
@RequiredArgsConstructor
public class MoradorController {

    private final MoradorService moradorService;

    @GetMapping
    public ResponseEntity<List<MoradorResponseDTO>> getMoradores(@ModelAttribute MoradorRequestDTO moradorRequestDTO) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(moradorService.pesquisarMorador(moradorRequestDTO)
                );

    }

    @GetMapping("/{guid}")
    public ResponseEntity<MoradorResponseDTO> getMorador(@PathVariable("guid") String guid) {

        return ResponseEntity.status(HttpStatus.OK).body(moradorService.recuperarMorador(guid));

    }

    @PostMapping
    public ResponseEntity<MoradorResponseDTO> salvarMorador(@RequestBody MoradorRequestDTO moradorRequestDTO) {

        return ResponseEntity.status(HttpStatus.OK).body(moradorService.cadastrarNovoMorador(moradorRequestDTO));
    }

    @PutMapping("/{guid}")
    public ResponseEntity<MoradorResponseDTO> atualizarMorador(@PathVariable("guid") String guid, @RequestBody MoradorRequestDTO moradorRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(moradorService.atualizarDadosMorador(moradorRequestDTO));
    }


    @DeleteMapping("/{guid}")
    public ResponseEntity excluirMorador(@PathVariable("guid") String guid) {
        moradorService.excluirMorador(guid);


        return ResponseEntity.ok().build();
    }
}
