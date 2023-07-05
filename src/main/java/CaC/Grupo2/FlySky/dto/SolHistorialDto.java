package CaC.Grupo2.FlySky.dto;

import CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolHistorialDto {
    long usuarioIdConsulta;
    long usuarioIdRespuenta;
}
