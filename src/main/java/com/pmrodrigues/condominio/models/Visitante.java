package com.pmrodrigues.condominio.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "visitantes")
@Entity
@Getter
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private String guid =  UUID.randomUUID().toString();

    @Column(name = "nome_do_visitante", nullable = false)
    @Setter
    private String nome;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name="apartamento_id", referencedColumnName = "id")
    private Apartamento apartamento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="data_visita")
    @Setter
    private LocalDateTime dataVisita = LocalDateTime.now();



}