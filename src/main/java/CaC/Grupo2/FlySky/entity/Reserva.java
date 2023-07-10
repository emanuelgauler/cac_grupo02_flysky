package CaC.Grupo2.FlySky.entity;
import CaC.Grupo2.FlySky.entity.Pago.Pago;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Getter
@Setter
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

    //@ManyToOne(cascade = CascadeType.PERSIST)
    //@JoinColumn(name = "vueloID", nullable = false)
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "reservaId")
    private List<Asiento> asientos;
    @Column(name = "vueloID")
    private Long vueloID;

    @Column(name = "estado_reserva")
    private boolean estadoReserva;

    @Column(name = "fecha_reserva")
    private Date fechaReserva;
    @Column(name = "monto")
    private double monto;

    @OneToOne(mappedBy = "reserva")
    private Pago pago;
}
