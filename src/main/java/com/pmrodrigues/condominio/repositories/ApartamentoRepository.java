package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Apartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartamentoRepository extends JpaRepository<Apartamento, Long> {

    Optional<Apartamento> findByGuid(final String guid);
}
