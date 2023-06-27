package CaC.Grupo2.FlySky.entity;
import javax.persistence.*;
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservaID")
    private Long reservaID;

    @Column(name = "estado_reserva")
    private boolean estadoReserva;

    @Column(name = "metodo_pago")
    private String metodoPago;
}
