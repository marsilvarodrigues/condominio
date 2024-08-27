package com.pmrodrigues.condominio.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "telefones")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    private final String guid = UUID.randomUUID().toString();

    @EqualsAndHashCode.Include
    @Column(name = "numero", nullable = false)
    @Setter
    private String numero;

    @JoinColumn(name = "morador_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Setter
    private Morador morador;
}
