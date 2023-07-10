package CaC.Grupo2.FlySky.entity;
import CaC.Grupo2.FlySky.entity.Pago.Pago;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservaID")
    private Long reservaID;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuarioID", nullable = false)
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "reservaId")
    private List<Asiento> asientos;
    @Column(name = "vueloID")
    private Long vueloID;

    @Column(name = "reserva_confirmada")
    private boolean reservaConfirmada;

    @Column(name = "fecha_reserva")
    private Date fechaReserva;
    @Column(name = "monto")
    private double monto;

    @OneToOne(mappedBy = "reserva")
    private Pago pago;
}
