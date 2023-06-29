package CaC.Grupo2.FlySky.dto;

import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.Vuelo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VueloDto {
    private List<AsientoDto> asientos;
    private String origen;
    private String destino;
    private Date fecha;
    private int precio;
    private boolean conexion;
    private String aerolinea;
   
   public static VueloDto of(Vuelo e) {
      VueloDto dto = new VueloDto();
      dto.aerolinea     = e.getAerolinea();
      dto.asientos      = e.getAsientos()
              .stream().map(AsientoDto::of)
              .collect(Collectors.toList());
      dto.conexion      = e.isConexion();
      dto.destino       = e.getDestino();
      dto.origen        = e.getOrigen();
      dto.fecha         = e.getFecha();
      dto.precio        = e.getPrecio();
      return dto;
   }
}
