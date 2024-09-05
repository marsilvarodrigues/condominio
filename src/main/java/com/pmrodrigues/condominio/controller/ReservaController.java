package com.pmrodrigues.condominio.controller;

import com.pmrodrigues.condominio.dto.EspacoComumDTO;
import com.pmrodrigues.condominio.dto.ReservaRequestDTO;
import com.pmrodrigues.condominio.dto.ReservaResponseDTO;
import com.pmrodrigues.condominio.services.ReservaService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/reservas")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping()
    public ResponseEntity<ReservaResponseDTO> efetuarReserva(@RequestBody ReservaRequestDTO reserva) {
        log.info("efetuando uma reserva {}", reserva);

        val response = reservaService.efetuarReserva(reserva);
        log.info("reserva {} efetuado com sucesso", response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(value="/{guid}")
    public ResponseEntity<ReservaResponseDTO> alterarReserva(@PathVariable("guid") @NonNull String guid, @RequestBody ReservaRequestDTO reserva) {

        log.info("efetuando uma alternacao na reserva {} - {}", guid, reserva);
        reserva = new ReservaRequestDTO(guid, reserva.espacaoComum(), reserva.morador(), reserva.dataReserva(), reserva.statusReserva());
        val response = reservaService.alterarReserva(reserva);
        log.info("reserva {} alterada com sucesso {}", guid, response);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping()
    public ResponseEntity<List<ReservaResponseDTO>> pesquisarReservaPorEspacoCoumn(@RequestParam("espacoComum") @NonNull String espacoComum) {

        log.info("efetuando pesquisa de resera por espaço comum {}", espacoComum);
        List<ReservaResponseDTO> reservas = reservaService.pesquisarReservasPorEspacoComum(new EspacoComumDTO(espacoComum, null, null, null));

        log.info("reservas encontradas {}", reservas);

        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

}
