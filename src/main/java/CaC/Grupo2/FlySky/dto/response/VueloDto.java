package CaC.Grupo2.FlySky.dto.response;

import CaC.Grupo2.FlySky.dto.request.AsientoDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
