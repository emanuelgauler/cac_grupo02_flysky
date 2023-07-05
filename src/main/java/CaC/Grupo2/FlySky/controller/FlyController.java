package CaC.Grupo2.FlySky.controller;

import CaC.Grupo2.FlySky.dto.ReservaDto;
import CaC.Grupo2.FlySky.dto.SolHistorialDto;
import CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum;
import CaC.Grupo2.FlySky.service.FlyService;
import CaC.Grupo2.FlySky.service.IFlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class FlyController {


    //@Qualifier("flyService")
    IFlyService flyService;

    public FlyController(FlyService flyService){
        this.flyService = flyService;
    }


    //User_Story_1
    /* Como usuario, quiero poder ver la lista de vuelos disponibles con su información detallada,
    como horarios, precios y aerolíneas, para poder elegir el vuelo que mejor se adapte a mis necesidades */
    @GetMapping("/getAllVuelos")
    public ResponseEntity<?> getAllVuelos(){
        return new ResponseEntity<>(flyService.buscarTodosVuelos(), HttpStatus.OK);
    }


    //User_Story_2
    /* Como usuario, quiero poder realizar una reserva de vuelo, ingresando los detalles requeridos,
    como nombres de pasajeros, fechas de viaje y preferencias de asientos, para asegurarme de tener
    un asiento reservado en el vuelo deseado */
    @PostMapping("/reservaVuelo")
    public ResponseEntity<?> guardarRerserva(@RequestBody ReservaDto reserva) throws ParseException {
        return new ResponseEntity<>(flyService.reservarVuelo(reserva),HttpStatus.OK);
    }

    /*
    @GetMapping("/Vuelo")
    public ResponseEntity<?> getVuelo(@PathVariable ){
        return new ResponseEntity<>(flyService.), HttpStatus.OK);
    }

     */

    @GetMapping("/getAllReservas")
    public ResponseEntity<?> getAllReservas(){
        return new ResponseEntity<>(flyService.buscarTodasReservas(), HttpStatus.OK);
    }

    //User_Story_4
    /* Como agente de ventas, quiero poder acceder al historial de reservas y preferencias de viaje
    de un cliente, para ofrecerle un servicio personalizado y promociones relevantes */
    @GetMapping("/getHistorial")
    public ResponseEntity<?> getHistorial(@RequestBody SolHistorialDto solHistorialDto) {
        return new ResponseEntity<>(flyService.getHistorial(solHistorialDto), HttpStatus.OK);
    }

}
