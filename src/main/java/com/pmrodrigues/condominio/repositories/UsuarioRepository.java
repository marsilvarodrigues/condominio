package com.pmrodrigues.condominio.repositories;

import com.pmrodrigues.condominio.models.Morador;
import com.pmrodrigues.condominio.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByGuid(String uuid);

    Optional<Usuario> findByUsername(String username);
}
