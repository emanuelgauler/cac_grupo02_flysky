package CaC.Grupo2.FlySky.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data

public class RespVentasDiarias {

    private int totalVentas;
    private Double ingresosGenerados;
}
