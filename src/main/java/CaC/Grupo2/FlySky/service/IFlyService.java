package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.ReservaDto;
import CaC.Grupo2.FlySky.dto.VueloDto;

import java.util.List;

public interface IFlyService {

    List<VueloDto> buscarTodosVuelos();

     String reservarVuelo(ReservaDto reservaDto);
}
