package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.*;
import CaC.Grupo2.FlySky.exception.NotFoundException;


import java.text.ParseException;
import java.util.List;

public interface IFlyService {

    List<VueloDto> buscarTodosVuelos();

    RespReservaDto reservarVuelo(ReservaDto reservaDto) throws ParseException;

    String pagarReserva(PagoDto pagoDto);

    /**
     * Este método sirve para que un <b>agente de ventas</b> puedas acceder al historial de viajes
     * de un confirmados (pagos) de un <b>cliente</b>
     * <ul>
     *   <li>El usuario quién consulta debe estar tipificado como <b>AGENTE_VENTAS</b> dentro de la tabla de usuarios.</li>
     *   <li>El usuario sobre quién se realiza la consulta debe estar tipificado como <b>CLIENTE</b> dentro de la tabla de usuarios.</li>
     * </ul>
     * @param solHistorialDto (ID de usuario que consulta y ID de usuario consultado)
     * @return Devuelve al payload los viajes que ha comprado el cliente: Origen, Destino, Aerolinea y Fecha de Vuelo.
     * @throws NotFoundException 5: (1-2) Quien consulta o sobre quien se realiza la consulta no existen en el sistema,
     * (3-4) Cuando los usuarios no cuentan con los permisos necesarios, y
     * (5) cuando el usuario no ha confirmado ningún viaje al momento de la consulta.
     */
   RtaHistorialDto getHistorial(SolHistorialDto solHistorialDto);
}
