package CaC.Grupo2.FlySky.dto.response;

import CaC.Grupo2.FlySky.dto.request.ReservaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespReservaDto {
    private ReservaDto reserva;
    private double monto;
    private String mensaje;
}
