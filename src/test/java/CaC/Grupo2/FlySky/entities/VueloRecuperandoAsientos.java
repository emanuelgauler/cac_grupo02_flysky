package CaC.Grupo2.FlySky.entities;

import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Vuelo;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class VueloRecuperandoAsientos {
   @Test
   void
   recuperando_asientos_del_vuelo() {
      Vuelo unVuelo = new Vuelo();
      Asiento asiento1 = un_asiento_nuevo("1P", 1L, unVuelo, "pasillo");
      Asiento asiento2 = un_asiento_nuevo("1V", 2L, unVuelo, "ventanilla");
      
      unVuelo.setVueloID(1L);
      unVuelo.setAerolinea("Aerolineas Argentinas");
      Date fecha_del_vuelo = new Date();
      
      unVuelo.setFecha( fecha_del_vuelo );
      unVuelo.setPrecio(45000);
      unVuelo.setOrigen("Bs. As.");
      unVuelo.setDestino("Mendoza");
      unVuelo.setConexion(false);
      List<Asiento> asientos = List.of(asiento1, asiento2);
      unVuelo.setAsientos( asientos );
      
      List<Asiento> asientos_del_vuelo = unVuelo.recuperarLosAsientosSegunLosIds(List.of(1L, 2L));
      
      assertThat(asientos_del_vuelo, containsInAnyOrder(asiento1, asiento2));
   }
   
   @Test void
   probar_que_retora_unicamente_los_asientos_apropieados_por_el_vuelo() {
      Vuelo unVuelo = new Vuelo();
      Asiento a1 = un_asiento_nuevo("1P", 1L, unVuelo, "pasillo");
      Asiento a2 = un_asiento_nuevo("1V", 2L, unVuelo, "ventanilla" );
      unVuelo.setAsientos(List.of(a1));
      
      List<Asiento> asientos_del_vuelo = unVuelo.recuperarLosAsientosSegunLosIds(List.of(1L, 2L));
      
      assertThat( asientos_del_vuelo, containsInAnyOrder( a1 ));
   }
   
   private static Asiento un_asiento_nuevo(String nombreAsiento, long asientoID, Vuelo unVuelo, String pasillo) {
      Asiento asiento1 = new Asiento();
      asiento1.setNombreAsiento(nombreAsiento);
      asiento1.setAsientoID(asientoID);
      asiento1.setOcupado(false);
      asiento1.setVuelo(unVuelo);
      asiento1.setPasajero(null);
      asiento1.setUbicacion(pasillo);
      return asiento1;
   }
}
