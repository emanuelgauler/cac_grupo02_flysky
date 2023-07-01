package CaC.Grupo2.FlySky.repository;


import CaC.Grupo2.FlySky.entity.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Long> {

}