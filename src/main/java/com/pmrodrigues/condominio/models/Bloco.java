package com.pmrodrigues.condominio.models;

import javax.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "blocos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bloco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private String guid =  UUID.randomUUID().toString();

    @Setter
    @Column(name = "nome_do_bloco" , nullable = false)
    private String nome;

    @Setter
    @Column(name = "numero_do_bloco" , nullable = false)
    private String numero;

    @Setter
    @Column(name = "quantidade_apartamento_bloco" , nullable = false)
    private Long quantidadeApartamento;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "bloco_id", referencedColumnName = "id")
    private final Set<Apartamento> apartamentos = new HashSet<>();

    @JoinColumn(name = "condominio_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Setter
    private Condominio condominio;

    public void adicionarApartamento(Apartamento apartamento) {
        this.apartamentos.add(apartamento);
        apartamento.setBloco(this);
    }
}


