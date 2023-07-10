package CaC.Grupo2.FlySky.dto;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

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

