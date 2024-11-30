package com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.ApartamentoDTO;
import com.pmrodrigues.condominio.dto.VisitanteRequestDTO;
import com.pmrodrigues.condominio.dto.VisitanteResponseDTO;
import com.pmrodrigues.condominio.exceptions.ApartamentoNotFoundException;
import com.pmrodrigues.condominio.exceptions.MoradorNotFoundException;
import com.pmrodrigues.condominio.exceptions.UsuarioNotFoundException;
import com.pmrodrigues.condominio.models.Veiculo;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class VisitanteService {

    private final VisitanteRepository visitanteRepository;

    private final ApartamentoRepository apartamentoRepository;

    private final MoradorRepository moradorRepository;

    private final UsuarioRepository usuarioRepository;

    public VisitanteResponseDTO informarEntradaDeVisitante(@NonNull VisitanteRequestDTO visitante) {

        log.info("informando a entrada do visitante {}", visitante);

        val apartamento = apartamentoRepository.findByGuid(visitante.apartamento())
                .orElseThrow(ApartamentoNotFoundException::new);
        val morador = moradorRepository.findByGuid(visitante.autorizadoPor())
                .orElseThrow(MoradorNotFoundException::new);
        val porteiro = usuarioRepository.findByGuid(visitante.registradoPor())
                .orElseThrow(UsuarioNotFoundException::new);

        val novaVisita = new Visitante();
        novaVisita.setDataVisita(visitante.dataDaVisita());
        novaVisita.setNome(visitante.nomeDoVisitante());
        novaVisita.setApartamento(apartamento);
        novaVisita.setAutorizadoPor(morador);
        novaVisita.setRegistradoPor(porteiro);

        if( visitante.veiculo() != null ) {
            val veiculo = new Veiculo();
            veiculo.setCor(visitante.veiculo().cor());
            veiculo.setModelo(visitante.veiculo().modelo());
            veiculo.setPlaca(visitante.veiculo().placa());
            veiculo.setFabricante(visitante.veiculo().fabricante());
            novaVisita.adicionaVeiculo(veiculo);
        }

        visitanteRepository.save(novaVisita);

        return VisitanteResponseDTO.fromVisita(novaVisita);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<VisitanteResponseDTO> recuperarVisitantesPorApartamento(@NonNull  ApartamentoDTO dto) {
        log.info("listando todas as visitas do apartamento {}", dto);
        val apartamento = apartamentoRepository.findByGuid(dto.apartamentoId())
                .orElseThrow(ApartamentoNotFoundException::new);

        return visitanteRepository.findByApartamento(apartamento)
                .stream()
                .map(VisitanteResponseDTO::fromVisita)
                .collect(Collectors.toList());
    }
}
