package CaC.Grupo2.FlySky.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class RtaHistorialDto {
    private String mensaje;

    private List<VueloDtoSA> vuelosUsuarios;

}