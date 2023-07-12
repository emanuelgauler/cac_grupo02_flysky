package CaC.Grupo2.FlySky.dto.response;

import CaC.Grupo2.FlySky.dto.request.AsientoDto;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDto {
    private String message;

    @Getter
    @Setter
    @Data
    @EqualsAndHashCode

    public static class VueloDto {
        private Long vueloID;
        private List<AsientoDto> asientos;
        private String origen;
        private String destino;
        private Date fecha;
        private double precio;
        private boolean conexion;
        private String aerolinea;
    }
}
