package ia.code.auth_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String celular;
    private String direccion;
    private Set<String> roles;
    private boolean enabled;
}
