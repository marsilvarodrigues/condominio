package com.pmrodrigues.condominio.models;

import javax.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "telefones")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    private String guid = UUID.randomUUID().toString();

    @EqualsAndHashCode.Include
    @Column(name = "numero", nullable = false)
    @Setter
    @With
    private String numero;

}
