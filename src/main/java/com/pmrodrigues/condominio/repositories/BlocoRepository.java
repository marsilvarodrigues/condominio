package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Bloco;
import com.pmrodrigues.condominio.models.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlocoRepository extends JpaRepository<Bloco, Long> {

    Optional<Bloco> findByGuid(final String guid);
}
