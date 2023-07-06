package CaC.Grupo2.FlySky.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDto {
    private Long numeroReserva;
    private UsuarioDto usuario;
    private String fechaReserva;
    private boolean pagado;
    private List<AsientoDto> asientos;
    private Long vueloID;

}
