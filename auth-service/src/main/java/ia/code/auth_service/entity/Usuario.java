package ia.code.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Column(unique = true, nullable = false, name = "nombre")
    private String nombre;
    @Column(unique = true, name = "celular")
    private String celular;
    @Column(nullable = false, name = "direccion")
    private String direccion;
    //Credenciales:
    @Column(unique = true, name = "correo")
    private String correo;
    @Column(nullable = false, name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
            (
                    name = "usuarios_roles",
                    joinColumns = @JoinColumn(name = "id_usuario"),
                    inverseJoinColumns = @JoinColumn(name = "id_rol")
            )
    private Set<Rol> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean enabled = true;
}
