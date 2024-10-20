package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.enums.StatusReserva;
import com.pmrodrigues.condominio.models.EspacoComum;
import com.pmrodrigues.condominio.models.Morador;
import com.pmrodrigues.condominio.models.Reserva;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByGuid(final String guid);

    @Query("SELECT COUNT(r) FROM Reserva r " +
            "WHERE r.morador = :morador " +
            " AND r.statusReserva = com.pmrodrigues.condominio.enums.StatusReserva.RESERVADO")
    Long quantasReservasAtivasExistemParaOMorador(@Param("morador") final Morador morador);

    @Query("SELECT r FROM Reserva r " +
            "JOIN FETCH r.morador m " +
            "JOIN FETCH r.espacoComum " +
            "WHERE r.espacoComum = :espacoComum " +
            "AND r.dataReserva = :dataReserva " +
            "AND r.statusReserva = :statusReserva")
    Optional<Reserva> findByEspacoComumDataReserva(@Param("espacoComum") EspacoComum espacoComum, @Param("dataReserva") Date dataReserva, @Param("statusReserva") StatusReserva statusReserva);

    @Query("SELECT r FROM Reserva r " +
            "JOIN FETCH r.morador m " +
            "JOIN FETCH r.espacoComum e " +
            "WHERE r.espacoComum = :espacoComum " +
            "AND r.dataReserva BETWEEN :inicioDoMes AND :fimDoMes " +
            "ORDER BY r.dataReserva ASC")
    List<Reserva> findByEspacoComum(@Param("espacoComum") @NonNull EspacoComum espacoComum,
                                    @Param("inicioDoMes") @NonNull Date inicioDoMes,
                                    @Param("fimDoMes") @NonNull Date fimDoMes);
}
