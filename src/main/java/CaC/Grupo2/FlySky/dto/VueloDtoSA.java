package CaC.Grupo2.FlySky.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode

public class VueloDtoSA {
        //private UsuarioDto usuario;
        //private Long vueloID;
        private String origen;
        private String destino;
        //private String fecha;
        //private double precio;
        //private boolean conexion;
        private String aerolinea;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date fecha;
    }

