package CaC.Grupo2.FlySky.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "vuelos")
public class Vuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vueloID")
    private Long vueloID;

    //@OneToMany(mappedBy = "vuelo",cascade = CascadeType.ALL)
    //@Column(name = "reservas")
    //private List<Reserva> reservas;

    @OneToMany(mappedBy = "vuelo",cascade = CascadeType.ALL)
    private List<Asiento> asientos;

    @Column(name = "origen")
    private String origen;

    @Column(name = "destino")
    private String destino;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "precio")
    private double precio;

    @Column(name = "conexion")
    private boolean conexion;

    @Column(name = "aerolinea")
    private String aerolinea;

}
