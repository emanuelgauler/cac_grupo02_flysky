package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.ReservaDto;
import CaC.Grupo2.FlySky.dto.VueloDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("flyService")
public class FlyService implements IFlyService{


    @Override
    public List<VueloDto> buscarTodosVuelos() {
        return null;
    }

    @Override
    public String reservarVuelo(ReservaDto reservaDto) {
        return "";
    }
}
