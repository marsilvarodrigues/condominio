package com.pmrodrigues.condominio.models;

import javax.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="veiculo_do_visitante")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Setter
    private String modelo;

    @Column(name = "cor", nullable = false)
    @Setter
    private String cor;

    @Column(name = "placa", nullable = false)
    @Setter
    private String placa;

    @Column(name="fabricante", nullable = false)
    @Setter
    private String fabricante;

}
