package CaC.Grupo2.FlySky.entity;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioID")
    private Long usuarioID;

    @Column(name = "tipo_usuario")
    private String tipoUsuario;

    @Column(name = "nombre_completo_usuario")
    private String nombreCompletoUsuario;

    @Column(name = "dni")
    private Long dni;
}
