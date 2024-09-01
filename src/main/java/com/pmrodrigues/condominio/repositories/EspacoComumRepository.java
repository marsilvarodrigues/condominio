package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.EspacoComum;
import com.pmrodrigues.condominio.models.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspacoComumRepository extends JpaRepository<EspacoComum, Long> {

    Optional<EspacoComum> findByGuid(final String guid);
}
