package com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.ApartamentoDTO;
import com.pmrodrigues.condominio.repositories.ApartamentoRepository;
import com.pmrodrigues.condominio.repositories.BlocoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.pmrodrigues.condominio.exceptions.BlocoNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApartamentoService {

    private final ApartamentoRepository apartamentoRepository;
    private final BlocoRepository blocoRepository;

    public List<ApartamentoDTO> listApartamentosPorBloco(String blocoId) {
        val bloco = blocoRepository.findByGuid(blocoId).orElseThrow(BlocoNotFoundException::new);
        return bloco.getApartamentos()
                .stream()
                .map(ApartamentoDTO::fromApartamento)
                .toList();
    }

    public List<ApartamentoDTO> listApartamentosPorBloco(String blocoId, String apartamento) {

        val bloco = blocoRepository.findByGuid(blocoId).orElseThrow(BlocoNotFoundException::new);

        return apartamentoRepository.findByBlocoAndNumeroLikeIgnoreCase(bloco, apartamento + '%')
                .stream()
                .map(ApartamentoDTO::fromApartamento)
                .toList();
    }
}


