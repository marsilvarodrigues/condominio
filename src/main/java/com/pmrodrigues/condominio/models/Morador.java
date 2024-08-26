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
@Setter
public class Morador extends Usuario{

    @Column(name = "nome_do_morador", nullable = false)
    private String nome;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_de_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "morador_id", referencedColumnName = "id")
    private Set<Telefone> telefones = new HashSet<>();

    public void adicionarTelefone(Telefone telefone) {
        this.telefones.add(telefone);
        telefone.setMorador(this);
    }

}
