package com.pmrodrigues.condominio.services;

import com.pmrodrigues.condominio.dto.EspacoComumDTO;
import com.pmrodrigues.condominio.dto.ReservaRequestDTO;
import com.pmrodrigues.condominio.dto.ReservaResponseDTO;
import com.pmrodrigues.condominio.enums.StatusReserva;
import com.pmrodrigues.condominio.exceptions.*;
import com.pmrodrigues.condominio.models.EspacoComum;
import com.pmrodrigues.condominio.models.Morador;
import com.pmrodrigues.condominio.models.Reserva;
import com.pmrodrigues.condominio.repositories.EspacoComumRepository;
import com.pmrodrigues.condominio.repositories.MoradorRepository;
import com.pmrodrigues.condominio.repositories.ReservaRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final MoradorRepository moradorRepository;
    private final EspacoComumRepository espacoComumRepository;

    public ReservaResponseDTO efetuarReserva(@NonNull ReservaRequestDTO reservaDTO) {
        log.info("efetuando a reserva {}",reservaDTO);

        LocalDate hoje = LocalDate.now();

        estaDentroDoPeriodoParaReserva(reservaDTO.dataReserva(), hoje);

        val espacoComumn = espacoComumRepository.findByGuid(reservaDTO.espacaoComum())
                .orElseThrow(EspacoComumNotFoundException::new);
        val morador = moradorRepository.findByGuid(reservaDTO.morador())
                .orElseThrow(MoradorNotFoundException::new);

        moradorEstourouAQuantidadeDeReservasPermitidas(morador);

        espacoComumJaReservado(reservaDTO, espacoComumn);

        val reserva = new Reserva();
        reserva.setEspacoComum(espacoComumn);
        reserva.setMorador(morador);
        reserva.setDataReserva(reservaDTO.dataReserva());

        reservaRepository.save(reserva);

        log.info("reserva {} salva", reserva);
        return ReservaResponseDTO.fromReserva(reserva);

    }

    public ReservaResponseDTO cancelarReserva(@NonNull ReservaRequestDTO reservaDTO) {
        log.info("efetuando o cancelamento da reserva {}", reservaDTO);

        if( reservaDTO.statusReserva() != StatusReserva.RESERVADO ) throw new AlteracaoNaoPermitadaException();

        val espacoComumn = espacoComumRepository.findByGuid(reservaDTO.espacaoComum())
                .orElseThrow(EspacoComumNotFoundException::new);
        val morador = moradorRepository.findByGuid(reservaDTO.morador())
                .orElseThrow(MoradorNotFoundException::new);
        val reserva = reservaRepository.findByGuid(reservaDTO.reservaId()).orElseThrow(ReservaNotFoundException::new);


        if( reserva.getEspacoComum().equals(espacoComumn) && reserva.getMorador().equals(morador) ) {

            LocalDate hoje = LocalDate.now();
            estaDentroDoPeriodoParaReserva(hoje, reserva.getDataReserva());

            reserva.setStatusReserva(StatusReserva.CANCELADO);
            reservaRepository.save(reserva);
            log.info("reserva {} salva", reserva);
            return ReservaResponseDTO.fromReserva(reserva);
        }
        throw new ReservaNotFoundException();
    }

    public ReservaResponseDTO alterarReserva(@NonNull ReservaRequestDTO reservaDTO) {
        log.info("efetuando a alteracao da reserva {}", reservaDTO);

        if( reservaDTO.statusReserva() != StatusReserva.RESERVADO ) throw new AlteracaoNaoPermitadaException();

        val espacoComumn = espacoComumRepository.findByGuid(reservaDTO.espacaoComum())
                .orElseThrow(EspacoComumNotFoundException::new);
        val morador = moradorRepository.findByGuid(reservaDTO.morador())
                .orElseThrow(MoradorNotFoundException::new);
        val reserva = reservaRepository.findByGuid(reservaDTO.reservaId()).orElseThrow(ReservaNotFoundException::new);


        if( reserva.getEspacoComum().equals(espacoComumn) && reserva.getMorador().equals(morador) ) {

            LocalDate hoje = LocalDate.now();
            estaDentroDoPeriodoParaReserva(hoje, reserva.getDataReserva());

            espacoComumJaReservado(reservaDTO, espacoComumn);

            reserva.setDataReserva(reservaDTO.dataReserva());
            reservaRepository.save(reserva);
            log.info("reserva {} salva", reserva);
            return ReservaResponseDTO.fromReserva(reserva);
        }
        throw new ReservaNotFoundException();
    }

    public List<ReservaResponseDTO> pesquisarReservasPorEspacoComum(@NonNull EspacoComumDTO espacoComumDTO) {

        val espacoComumn = espacoComumRepository.findByGuid(espacoComumDTO.guid())
                .orElseThrow(EspacoComumNotFoundException::new);

        val hoje = LocalDate.now();
        val inicioDoMes = hoje.with(TemporalAdjusters.firstDayOfMonth());
        val fimDoMes = hoje.with(TemporalAdjusters.lastDayOfMonth());

        return reservaRepository.findByEspacoComum(espacoComumn, inicioDoMes, fimDoMes)
                .stream()
                .map(ReservaResponseDTO::fromReserva)
                .collect(Collectors.toList());

    }

    private void espacoComumJaReservado(@NonNull ReservaRequestDTO reservaDTO, EspacoComum espacoComumn) {
        val reserva = reservaRepository.findByEspacoComumDataReserva(espacoComumn, reservaDTO.dataReserva(), StatusReserva.RESERVADO);
        if( reserva.isPresent() ) throw new EspacoBloqueadoParaException();
    }

    private void moradorEstourouAQuantidadeDeReservasPermitidas(Morador morador) {
        val quantidadeDeReservasAtivas = reservaRepository.quantasReservasAtivasExistemParaOMorador(morador);
        if( quantidadeDeReservasAtivas >= 2) throw new EstouroDeReservasAtivasException();
    }

    private void estaDentroDoPeriodoParaReserva(@NonNull LocalDate dataIncial, @NonNull LocalDate dataFinal) {

        val horasDeAntecedencia = ChronoUnit.DAYS.between(dataIncial, dataFinal);
        if( horasDeAntecedencia < 1 ) throw new ForaDoPrazoParaReservarException();
    }
}
