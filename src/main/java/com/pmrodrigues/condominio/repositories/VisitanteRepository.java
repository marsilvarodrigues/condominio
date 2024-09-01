package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Apartamento;
import com.pmrodrigues.condominio.models.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {

    Optional<Visitante> findByGuid(final String guid);
}
