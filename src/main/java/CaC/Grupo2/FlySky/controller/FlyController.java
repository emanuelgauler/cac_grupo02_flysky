package CaC.Grupo2.FlySky.controller;

import CaC.Grupo2.FlySky.dto.request.PagoDto;
import CaC.Grupo2.FlySky.dto.request.ReservaDto;
import CaC.Grupo2.FlySky.dto.request.SolHistorialDto;
import CaC.Grupo2.FlySky.dto.request.SolVentasDiariasDto;
import CaC.Grupo2.FlySky.service.FlyService;
import CaC.Grupo2.FlySky.service.IFlyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class FlyController {


    IFlyService flyService;

    public FlyController(FlyService flyService){
        this.flyService = flyService;
    }


    //User_Story_1
    @GetMapping("/getAllVuelos")
    public ResponseEntity<?> getAllVuelos(){
        return new ResponseEntity<>(flyService.buscarTodosVuelos(), HttpStatus.OK);
    }

    //User_Story_2
    @PostMapping("/reservaVuelo")
    public ResponseEntity<?> guardarRerserva(@RequestBody ReservaDto reserva) throws ParseException {
        return new ResponseEntity<>(flyService.reservarVuelo(reserva),HttpStatus.OK);
    }

    //User_Story_3
    @PostMapping("/pagarReserva")
    public ResponseEntity<?> pagarVuelo(@RequestBody PagoDto pagoDto){
        return new ResponseEntity<>(flyService.pagarReserva(pagoDto), HttpStatus.OK);
    }


    //User_Story_4
    /* Como agente de ventas, quiero poder acceder al historial de reservas y preferencias de viaje
    de un cliente, para ofrecerle un servicio personalizado y promociones relevantes */
    @GetMapping("/getHistorial")
    public ResponseEntity<?> getHistorial(@RequestBody SolHistorialDto solHistorialDto) {
        return new ResponseEntity<>(flyService.getHistorial(solHistorialDto), HttpStatus.OK);
    }

    //User_Story_5
    @GetMapping("/getVentasDiarias")
    public ResponseEntity<?> getVentasDiarias(@RequestBody SolVentasDiariasDto solVentasDiarias) {
        return new ResponseEntity<>(flyService.getVentasDiarias(solVentasDiarias), HttpStatus.OK);
    }

}
