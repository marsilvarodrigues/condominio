package com.pmrodrigues.condominio.models;

import javax.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "apartamentos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "apartamento_id", referencedColumnName = "id")
    private Set<Morador> moradores = new HashSet<>();

}
