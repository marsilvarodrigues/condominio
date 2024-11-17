package com.pmrodrigues.condominio.models;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="perfis")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    private String guid;

    @Column(name = "perfil", unique = true, length = 20, nullable = false)
    private String authority;

    @PrePersist
    public void preInsert() {
        this.guid = UUID.randomUUID().toString();
    }

}
