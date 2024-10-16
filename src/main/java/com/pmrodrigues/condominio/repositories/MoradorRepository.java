package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Morador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Long>, JpaSpecificationExecutor<Morador> {

    Optional<Morador> findByGuid(String uuid);

    Optional<Morador> findByCpf(String cpf);

    Optional<Morador> findByEmail(String email);


}
