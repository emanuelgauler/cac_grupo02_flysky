package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.request.*;
import CaC.Grupo2.FlySky.dto.response.ErrorDto;
import CaC.Grupo2.FlySky.dto.response.RespReservaDto;
import CaC.Grupo2.FlySky.dto.response.RespVentasDiarias;
import CaC.Grupo2.FlySky.dto.response.RtaHistorialDto;
import CaC.Grupo2.FlySky.exception.IllegalArgumentException;

import java.text.ParseException;
import java.util.List;

public interface IFlyService {
    /**
     * Este método sirve para que los <b>Usarios</b> puedan acceder a la <b>lista de vuelos disponibles</b> con su información detallada, como horarios, precios y aerolíneas
     *
     * @return Devuelve los vuelos disponible con la siguiente informacion:
     * <ul>
     *     <li>Lista de asientos</li>
     *     <li>Origen</li>
     *     <li>Destino</li>
     *     <li>Fecha</li>
     *     <li>Precio</li>
     *     <li>Conexion</li>
     *     <li>Aerolinea</li>
     * </ul>
     *
     * @throws NotFoundException cuando la lista de vuelos esta vacía.
     *
     */
    List<ErrorDto.VueloDto> buscarTodosVuelos();

    /**
     * Este método sirve para que los <b>Usarios tipo Cliente</b> puedan reservar un Vuelo
     *
     * @param ReservaDto (ID de usuario que realiza la reserva, vueloID, Lista de asiento a ocupar con su informacion detallada)
     * @return Devuelve la reserva Realizada con la siguiente informacion:
     * <ul>
     *     <li>Numero de Reserva</li>
     *     <li>UsuarioId que realizo la reserva</li>
     *     <li>Fecha de la Reserva</li>
     *     <li>isPagado</li>
     *     <li>List Asientos</li>
     *     <li>vueloID</li>
     *     <li>Su reserva se realizo con exito... Tienes 10 minutos para realizar el pago</li>
     * </ul>
     *
     * @throws NotFoundException cuando el usuarioID que intenta reservar no existe.
     * <br>
     * @throws IllegalArgumentException cuando ocurre alguno de los siguientes casos:
     * <ol>
     *     <li>cuando el usuarioID no es de tipo cliente</li>
     *     <li>No se encontro el vuelo con el ID especificado que intenta reservar</li>
     *     <li>el vuelo que intenta reserva su fecha ya caduco</li>
     *     <li>No se encontró el asiento con el ID especificado</li>
     *     <li>el asiento ya se encuentra ocupado</li>
     * </ol>
     */
    RespReservaDto reservarVuelo(ReservaDto reservaDto) throws ParseException;

    /**
     * Este método sirve para que los <b>Usarios tipo Cliente</b> puedan Pagar la Reserva
     *
     * @param PagoDto (el cual contine una reservaID, un tipo Pago es cual es un Enum (tarjeta_debito, tarjeta_credito, efectivo, transferencia) y monto)
     * @return String Reserva pagada exitosamente
     *
     * @throws NotFoundException cuando No se encontro la reserva con el ID especificado.
     * <br>
     * @throws IllegalArgumentException cuando ocurre alguno de los siguientes casos:
     * <ol>
     *     <li>cuando ya se realizo el pago de la reserva que se esta intentando pagar</li>
     *     <li>cuando el tiempo maximos para pagar 10 min vencio</li>
     *     <li>cuando no se ingrese el monto correcto</li>
     * </ol>
     */
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

    /**
     * Este método sirve para que un <b>Administrador</b> puedas acceder al total de <b>ventas diarias<b> y <b>Ingresos diarios.<b>
     * <br>
     * <br>
     * El usuario quién consulta debe estar tipificado como <b>ADMINISTRADOR</b> dentro de la tabla de usuarios.
     * @param solVentasDiariasDto (ID del usuario Aministrador)
     * @return Devuelve el total Ventas y ingresos generados del dia corriente.
     * @throws NotFoundException el usuario que esta intentando realizar la consulta no existe en el sistema.
     * @throws IllegalArgumentException cuando el usuario que consulta no es ADMINISTRADOR
     *
     */
    RespVentasDiarias getVentasDiarias(SolVentasDiariasDto solVentasDiarias);

}
