package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.*;

import java.text.ParseException;
import java.util.List;

public interface IFlyService {

    List<VueloDto> buscarTodosVuelos();

    RespReservaDto reservarVuelo(ReservaDto reservaDto) throws ParseException;

    List<ReservaDto> buscarTodasReservas();

    List<RespReservaDto> getHistorial(SolHistorialDto solHistorialDto);
}
