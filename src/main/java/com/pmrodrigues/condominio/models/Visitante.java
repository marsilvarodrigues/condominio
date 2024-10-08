package com.pmrodrigues.condominio.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "visitantes")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private String guid =  UUID.randomUUID().toString();

    @Column(name = "nome_do_visitante", nullable = false)
    @Setter
    private String nome;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name="apartamento_id", referencedColumnName = "id")
    private Apartamento apartamento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="data_visita")
    @Setter
    private LocalDateTime dataVisita = LocalDateTime.now();

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    @JoinColumn(name = "registrado_por_id", referencedColumnName = "id")
    private Usuario registradoPor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    @JoinColumn(name = "autorizado_por_id", referencedColumnName = "id")
    private Morador autorizadoPor;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "visitante")
    @JoinColumn(name = "visitante_id", referencedColumnName = "id")
    private Veiculo veiculo;

    public void adicionaVeiculo(@NonNull Veiculo veiculo) {
        this.veiculo = veiculo;
        veiculo.setVisitante(this);
    }


}
