package CaC.Grupo2.FlySky.entity.Pago;

import CaC.Grupo2.FlySky.entity.Reserva;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagoID")
    private Long pagoID;
    @OneToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @Column(name = "fecha_pago")
    private Date fechaPago;
    @Column(name = "tipo_pago")
    private TipoPago tipoPago;

    @Column(name = "monto")
    private double monto;

    @Column(name = "pagado", columnDefinition = "boolean default false")
    private boolean pagado;

}
