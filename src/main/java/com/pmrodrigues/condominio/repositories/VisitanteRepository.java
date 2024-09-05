package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Apartamento;
import com.pmrodrigues.condominio.models.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {

    Optional<Visitante> findByGuid(final String guid);

    @Query("SELECT v FROM Visitante v " +
            "LEFT JOIN FETCH v.veiculo " +
            "WHERE v.apartamento =:apartamento " +
            "ORDER BY v.dataVisita DESC")
    List<Visitante> findByApartamento(@Param("apartamento") final Apartamento apartamento);

}
