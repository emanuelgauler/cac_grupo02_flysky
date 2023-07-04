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
    
    @ManyToMany
    @JoinColumn(name = "asiento", nullable = false )
    private List<Asiento> asientos;

    /*
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vueloID", nullable = false)
    private Vuelo vuelo;
     */

    @Column(name = "estado_reserva")
    private boolean estadoReserva;

    @Column(name = "fecha_reserva")
    private Date fechaReserva;

    @OneToOne(mappedBy = "reserva")
    private Pago pago;
}
