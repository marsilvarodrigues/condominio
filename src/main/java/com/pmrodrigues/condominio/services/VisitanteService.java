package com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.VisitanteDTO;
import com.pmrodrigues.condominio.exceptions.ApartamentoNotFoundException;
import com.pmrodrigues.condominio.exceptions.MoradorNotFoundException;
import com.pmrodrigues.condominio.exceptions.UsuarioNotFoundException;
import com.pmrodrigues.condominio.models.Visitante;
import com.pmrodrigues.condominio.repositories.ApartamentoRepository;
import com.pmrodrigues.condominio.repositories.MoradorRepository;
import com.pmrodrigues.condominio.repositories.UsuarioRepository;
import com.pmrodrigues.condominio.repositories.VisitanteRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisitanteService {

    private final VisitanteRepository visitanteRepository;

    private final ApartamentoRepository apartamentoRepository;

    private final MoradorRepository moradorRepository;

    private final UsuarioRepository usuarioRepository;

    public VisitanteDTO informarEntradaDeVisitante(@NonNull VisitanteDTO visitante) {

        log.info("informando a entrada do visitante {}", visitante);

        val apartamento = apartamentoRepository.findByGuid(visitante.apartamento().apartamentoId()).orElseThrow(ApartamentoNotFoundException::new);
        val morador = moradorRepository.findByGuid(visitante.autorizadoPor().guid()).orElseThrow(MoradorNotFoundException::new);
        val porteiro = usuarioRepository.findByGuid(visitante.registradoPor().usuarioId()).orElseThrow(UsuarioNotFoundException::new);

        val novaVisita = new Visitante();
        novaVisita.setDataVisita(visitante.dataDaVisita());
        novaVisita.setNome(visitante.nomeDoVisitante());
        novaVisita.setApartamento(apartamento);
        novaVisita.setAutorizadoPor(morador);
        novaVisita.setRegistradoPor(porteiro);

        visitanteRepository.save(novaVisita);

        return VisitanteDTO.fromVisita(novaVisita);

    }
}
