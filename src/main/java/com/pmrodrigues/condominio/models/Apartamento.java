package com.pmrodrigues.condominio.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Entity
@Table(name = "apartamentos")
@Getter
public class Apartamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private String guid =  UUID.randomUUID().toString();

    @Setter
    @Column(name = "numero")
    private String numero;

    @Setter
    @Column(name = "andar")
    private Long andar;

    @JoinColumn(name = "bloco_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Setter
    private Bloco bloco;

}