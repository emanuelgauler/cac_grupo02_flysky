package CaC.Grupo2.FlySky.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class VueloDtoSA {

        private String origen;
        private String destino;

        private String aerolinea;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date fecha;
    }

