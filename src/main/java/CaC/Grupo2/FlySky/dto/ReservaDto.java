package CaC.Grupo2.FlySky.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDto {
    private Long numeroReserva;
    private UsuarioDto usuario;
    private VueloDto vuelo;
    private String fechaReserva;
    private boolean pagado;

}
