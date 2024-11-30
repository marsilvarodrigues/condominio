package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Apartamento;
import com.pmrodrigues.condominio.models.Bloco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ApartamentoRepository extends JpaRepository<Apartamento, Long> {

    Optional<Apartamento> findByGuid(final String guid);

    Collection<Apartamento> findByBloco(Bloco bloco);

    Collection<Apartamento> findByBlocoAndNumeroLikeIgnoreCase(Bloco bloco, String apartamento);
}
