package com.pmrodrigues.condominio.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "morador")
@Getter
public class Morador extends Usuario{

    @Column(name = "nome_do_morador", nullable = false)
    @Setter
    private String nome;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_de_nascimento", nullable = false)
    @Setter
    private LocalDate dataNascimento;

    @Column(name = "email", unique = true, nullable = false)
    @Setter
    private String email;

    @Column(name = "cpf", unique = true, nullable = false)
    @Setter
    private String cpf;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "morador_id", referencedColumnName = "id")
    private final Set<Telefone> telefones = new HashSet<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Setter
    private Apartamento apartamento;

    public void adicionarTelefone(Telefone telefone) {
        this.telefones.add(telefone);
        telefone.setMorador(this);
    }

}
