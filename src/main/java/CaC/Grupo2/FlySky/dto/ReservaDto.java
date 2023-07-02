package CaC.Grupo2.FlySky.dto;

import CaC.Grupo2.FlySky.entity.Vuelo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDto {
    private Long reservaID;
    private UsuarioDto usuario;
    private VueloDto vuelo;
    private Date fechaReserva;

}
