package CaC.Grupo2.FlySky.dto.request;

import CaC.Grupo2.FlySky.entity.Pago.TipoPago;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class PagoDto {
    private long reservaID;
    private TipoPago tipoPago;
    private double monto;

}
