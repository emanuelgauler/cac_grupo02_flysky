package CaC.Grupo2.FlySky.repository;

import CaC.Grupo2.FlySky.entity.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlyRepository extends JpaRepository<Vuelo,Long> {

}

