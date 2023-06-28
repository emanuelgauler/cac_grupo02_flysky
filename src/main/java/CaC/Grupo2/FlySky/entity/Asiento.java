package CaC.Grupo2.FlySky.entity;

import javax.persistence.*;

@Entity
@Table(name = "asiento")
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asientoID")
    private Long asientoID;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vueloID", nullable = false)
    private Vuelo vuelo;

    @Column(name = "nombre_completo_usuario")
    private String nombreCompletoUsuario;

    @Column(name = "ocupado")
    private boolean ocupado;

    @Column(name = "ubicacion")
    private String ubicacion;

}
