package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.ReservaDto;
import CaC.Grupo2.FlySky.dto.VueloDto;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.repositories.IFlyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("flyService")
public class FlyService implements IFlyService{
    
    
    private final IFlyRepository repository;
    
    public FlyService(IFlyRepository repo) {
        this.repository = repo;
    }
    
    @Override
    public List<VueloDto> buscarTodosVuelos() {
        List<Vuelo> vuelos = repository.buscarVuelos();
        return vuelos.stream().map(VueloDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public String reservarVuelo(ReservaDto reservaDto) {
        return "";
    }
}
