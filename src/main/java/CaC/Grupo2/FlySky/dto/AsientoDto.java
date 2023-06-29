package CaC.Grupo2.FlySky.dto;

import CaC.Grupo2.FlySky.entity.Asiento;

import javax.persistence.Column;

public class AsientoDto {
    String pasajero;
    private boolean ocupado;
    private String ubicacion;
    
    public static AsientoDto of(Asiento e) {
        AsientoDto dto  = new AsientoDto();
        dto.pasajero    = e.getNombreCompletoUsuario();
        dto.ubicacion   = e.getUbicacion();
        dto.ocupado     = e.isOcupado();
        return dto;
    }
}
