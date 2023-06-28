package CaC.Grupo2.FlySky.controller;

import CaC.Grupo2.FlySky.service.FlyService;
import CaC.Grupo2.FlySky.service.IFlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class FlyController {

    @Autowired
    @Qualifier("flyService")
    IFlyService flyService;


    //User_Story_1
    @GetMapping("/getAllVuelos")
    public ResponseEntity<?> getAllVuelos(){
        return new ResponseEntity<>(flyService.buscarTodosVuelos(), HttpStatus.OK);
    }

}
