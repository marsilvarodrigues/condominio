package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Telefone;
import com.pmrodrigues.condominio.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    Optional<Telefone> findByGuid(String uuid);
}
