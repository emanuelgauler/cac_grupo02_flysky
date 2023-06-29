package CaC.Grupo2.FlySky.service.flyservice;

import CaC.Grupo2.FlySky.dto.VueloDto;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.repositories.IFlyRepository;
import CaC.Grupo2.FlySky.service.FlyService;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlyServiceRetornandoLosVuelos {
   
   @Test void
   cuando_la_BD_esta_vacia__debe_retornar_una_lista_vacia_de_DTO(){
      IFlyRepository repo = mock(IFlyRepository.class);
      when(repo.buscarVuelos()).thenReturn(new ArrayList<Vuelo>());
      FlyService fly_service = new FlyService(repo);
      
      List<VueloDto> flies = fly_service.buscarTodosVuelos();
      
      assertThat( flies, is(empty()) );
   }
}
