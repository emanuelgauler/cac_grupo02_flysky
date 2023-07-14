package CaC.Grupo2.FlySky.repository;


import CaC.Grupo2.FlySky.entity.Pago.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

}