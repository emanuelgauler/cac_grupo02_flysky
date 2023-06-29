package CaC.Grupo2.FlySky.repositories;

import CaC.Grupo2.FlySky.entity.Vuelo;

import java.util.List;

public interface IFlyRepository {
   List<Vuelo> buscarVuelos();
}
