package com.pmrodrigues.condominio.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "morador")
@Getter
@NoArgsConstructor
@AllArgsConstructor()
@SuperBuilder()
public class Morador extends Usuario{

    @Column(name = "nome_do_morador", nullable = false)
    @Setter
    @With
    private String nome;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_de_nascimento", nullable = false)
    @Setter
    @With
    private Date dataNascimento;


    @Column(name = "email", unique = true, nullable = false)
    @Setter
    @Email
    @With
    private String email;

    @Column(name = "cpf", unique = true, nullable = false)
    @Setter
    @CPF(message = "CPF informado é inválido")
    @With
    private String cpf;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Telefone.class)
    @JoinColumn(name = "morador_id", referencedColumnName = "id")
    @Builder.Default
    @With
    private final Set<Telefone> telefones = new HashSet<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Setter
    @With
    private Apartamento apartamento;

    public void adicionarTelefone(Collection<Telefone> telefones) {
        this.telefones.clear();
        this.telefones.addAll(telefones);
    }


}
