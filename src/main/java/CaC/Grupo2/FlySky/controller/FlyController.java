package CaC.Grupo2.FlySky.controller;

import CaC.Grupo2.FlySky.dto.ReservaDto;
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
    @GetMapping("/getAllVuelos")
    public ResponseEntity<?> getAllVuelos(){
        return new ResponseEntity<>(flyService.buscarTodosVuelos(), HttpStatus.OK);
    }


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



}
