package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Perfil;
import com.pmrodrigues.condominio.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByGuid(String uuid);

    List<Perfil> findAllByAuthorityIn(List<String> perfis);
}
