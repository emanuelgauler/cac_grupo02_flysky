package CaC.Grupo2.FlySky.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
@Entity
@Table(name = "vuelos")
public class Vuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vueloID")
    private Long vueloID;

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
    
    public List<Asiento> recuperarLosAsientosSegunLosIds(List<Long> asientosSolicitados) {
        
        List<Asiento> asientos_solicitados
                = asientos.stream()
                .filter(asiento -> asientosSolicitados.contains(asiento.getAsientoID()))
                .collect(Collectors.toList());
        
        return asientos_solicitados;
    }
}
