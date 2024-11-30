package com.pmrodrigues.condominio.models;

import javax.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "espacos_comuns")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
