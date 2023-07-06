package CaC.Grupo2.FlySky.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
@Getter
@Setter
public class AsientoDto {
    private Long asientoID;
   private String nombreAsiento;
    private String pasajero;
    private boolean ocupado;
    private String ubicacion;
}
