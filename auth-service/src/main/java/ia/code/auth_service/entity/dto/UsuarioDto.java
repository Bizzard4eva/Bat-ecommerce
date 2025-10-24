package ia.code.auth_service.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idUsuario;
    private String nombre;
}
