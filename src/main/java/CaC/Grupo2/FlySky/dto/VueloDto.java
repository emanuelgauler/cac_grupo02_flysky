package CaC.Grupo2.FlySky.dto;

import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Reserva;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

public class VueloDto {
    private List<AsientoDto> asientos;
    private String origen;
    private String destino;
    private Date fecha;
    private int precio;
    private boolean conexion;
    private String aerolinea;
}
