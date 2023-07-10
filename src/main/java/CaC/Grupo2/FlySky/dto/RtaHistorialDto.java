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
public class RtaHistorialDto {
    //private List<UsuarioDto>;
    private List<VueloDto> vuelosUsuarios;
    private String mensaje;
}