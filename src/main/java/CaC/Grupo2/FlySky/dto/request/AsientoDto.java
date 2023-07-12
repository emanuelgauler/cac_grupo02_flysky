package CaC.Grupo2.FlySky.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AsientoDto {
    private Long asientoID;
   private String nombreAsiento;
    private String pasajero;
    private boolean ocupado;
    private String ubicacion;
}
