package CaC.Grupo2.FlySky.entity;
import javax.persistence.*;
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservaID")
    private Long reservaID;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuarioID", nullable = false)
    private Usuario usuario;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vueloID", nullable = false)
    private Vuelo vuelo;

    @Column(name = "estado_reserva")
    private boolean estadoReserva;

    @Column(name = "metodo_pago")
    private String metodoPago;
}
