package CaC.Grupo2.FlySky.repository;

import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
}
