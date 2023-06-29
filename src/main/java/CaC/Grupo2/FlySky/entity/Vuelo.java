package CaC.Grupo2.FlySky.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vuelo")
@Data
public class Vuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vueloID")
    private Long vueloID;

    @OneToMany(mappedBy = "vuelo",cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "vuelo",cascade = CascadeType.ALL)
    private List<Asiento> asientos;

    @Column(name = "origen")
    private String origen;

    @Column(name = "destino")
    private String destino;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "precio")
    private int precio;

    @Column(name = "conexion")
    private boolean conexion;

    @Column(name = "aerolinea")
    private String aerolinea;

}
