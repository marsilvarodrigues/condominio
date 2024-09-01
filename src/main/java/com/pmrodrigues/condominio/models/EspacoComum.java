package com.pmrodrigues.condominio.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "espacos_comuns")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EspacoComum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String guid = UUID.randomUUID().toString();

    @Column(name = "nome", nullable = false)
    @Setter
    private String nome;

    @Column(name = "descricao")
    @Setter
    private String descricao;

    @Column(name = "capacidade_maxima", nullable = false)
    @Setter
    private Integer capacidadeMaxima;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "condominio_id", nullable = false)
    @Setter
    private Condominio condominio;
}
