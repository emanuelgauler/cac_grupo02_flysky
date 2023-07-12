package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.request.*;
import CaC.Grupo2.FlySky.dto.response.RespReservaDto;
import CaC.Grupo2.FlySky.dto.response.RtaHistorialDto;


import java.text.ParseException;
import java.util.List;

public interface IFlyService {

    List<VueloDto> buscarTodosVuelos();

    RespReservaDto reservarVuelo(ReservaDto reservaDto) throws ParseException;

    String pagarReserva(PagoDto pagoDto);

    RtaHistorialDto getHistorial(SolHistorialDto solHistorialDto);

    Object getVentasDiarias(SolVentasDiarias solVentasDiarias);
}
