package CaC.Grupo2.FlySky.entity.usuario;

import CaC.Grupo2.FlySky.entity.Reserva;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@Getter
@Setter
@Entity
@Data
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioID")
    private Long usuarioID;

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    @Column(name = "tipo_usuario")
    private TipoUsuarioEnum tipoUsuario;

    @Column(name = "nombre_completo_usuario")
    private String nombreCompletoUsuario;

    @Column(name = "telefono")
    private Long telefono;

   }

