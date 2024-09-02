package com.pmrodrigues.condominio.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="veiculo_do_visitante")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    private String guid =  UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "visitante_id", referencedColumnName = "id")
    @Setter
    private Visitante visitante;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "cor", nullable = false)
    private String cor;

    @Column(name = "placa", nullable = false)
    private String placa;

}