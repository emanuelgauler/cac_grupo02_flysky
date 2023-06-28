package CaC.Grupo2.FlySky.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioID")
    private Long usuarioID;

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    @Column(name = "tipo_usuario")
    private String tipoUsuario;

    @Column(name = "nombre_completo_usuario")
    private String nombreCompletoUsuario;

    @Column(name = "telefono")
    private Long telefono;

   }
