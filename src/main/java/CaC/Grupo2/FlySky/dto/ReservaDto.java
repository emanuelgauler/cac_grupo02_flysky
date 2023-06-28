package CaC.Grupo2.FlySky.dto;

import CaC.Grupo2.FlySky.entity.Vuelo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

public class ReservaDto {
    private UsuarioDto usuario;
    private VueloDto vueloDto;
    private Date fechaReserva;

}
