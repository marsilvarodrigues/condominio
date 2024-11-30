package com.pmrodrigues.condominio.models;

import javax.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "condominio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Condominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private String guid =  UUID.randomUUID().toString();

    @Column(name = "nome_do_condominio", nullable = false)
    private String nome;

    @Column(name = "quantidade_de_blocos" , nullable = false)
    private Long quantidadeBlocos;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "condominio_id", referencedColumnName = "id")
    private Set<Bloco> blocos = new HashSet<>();

    public void adicionarBloco(Bloco bloco) {
        this.blocos.add(bloco);
        bloco.setCondominio(this);
    }
}
