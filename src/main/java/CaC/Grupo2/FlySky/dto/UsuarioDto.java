package CaC.Grupo2.FlySky.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
@Getter
@Setter
public class UsuarioDto {
    private String nombreCompletoUsuario;
    private Long telefono;
}
