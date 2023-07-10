package CaC.Grupo2.FlySky.dto;

import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Reserva;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@Data
@EqualsAndHashCode

public class VueloDto {
    private Long vueloID;
    private List<AsientoDto> asientos;
    private String origen;
    private String destino;
    private Date fecha;
    private double precio;
    private boolean conexion;
    private String aerolinea;
}
