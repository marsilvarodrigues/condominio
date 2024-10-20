package com.pmrodrigues.condominio.models;

import com.pmrodrigues.condominio.enums.StatusReserva;
import javax.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "reservas")
@Getter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid", unique = true, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private String guid =  UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "espaco_comum_id", referencedColumnName = "id")
    @Setter
    @ToString.Include
    private EspacoComum espacoComum;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "morador_id", referencedColumnName = "id")
    @Setter
    @ToString.Include
    private Morador morador;

    @Temporal(TemporalType.DATE)
    @Setter
    @Column(name="data_reserva", nullable = false)
    @ToString.Include
    private Date dataReserva;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="status_reserva")
    @Setter
    @ToString.Include
    private StatusReserva statusReserva = StatusReserva.RESERVADO;
}
