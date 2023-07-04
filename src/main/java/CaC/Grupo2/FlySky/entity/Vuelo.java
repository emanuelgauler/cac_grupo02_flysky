package CaC.Grupo2.FlySky.entity;

import CaC.Grupo2.FlySky.exception.IllegalArgumentException;
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
    
    public List<Asiento> recuperaTusAsientosSegunLos(List<Asiento> asientosSolicitados) {
        List<Long> id_asientos_solicitados = asientosSolicitados.stream()
                .map(Asiento::getAsientoID)
                .collect(Collectors.toList());
        
        List<Asiento> asientos_solicitados = new ArrayList<>();
        for( Long id : id_asientos_solicitados ) {
            Optional<Asiento> first = asientos.stream().filter(asiento -> asiento.getAsientoID().equals(id))
                    .findFirst();
            first.ifPresent(asientos_solicitados::add);
        }
        
        List<Long> collect = id_asientos_solicitados.stream()
                .filter(id -> asientos_solicitados.stream()
                        .anyMatch(asiento -> !asiento.getAsientoID().equals(id))
                ).collect(Collectors.toList());
        if( 0 < collect.size()) {
            String message = collect.stream().map( id -> String.format("El asiento %i no existe en el vuelo %i\n", id, getVueloID()) )
                    .reduce( "", String::concat);
            
            throw new IllegalArgumentException( message );
        }
        
        return asientos_solicitados;
    }
}
