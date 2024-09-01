package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CondominioRepository extends JpaRepository<Condominio, Long> {

    Optional<Condominio> findByGuid(final String guid);
}
