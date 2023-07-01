package CaC.Grupo2.FlySky.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "asientos")
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asientoID")
    private Long asientoID;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vueloID", nullable = false)
    private Vuelo vuelo;

    @Column(name = "pasajero")
    private String pasajero;
    @Column(name = "nombre_asiento")
    private String nombreAsiento;

    @Column(name = "ocupado")
    private boolean ocupado;

    @Column(name = "ubicacion")
    private String ubicacion;

}
