package ia.code.auth_service.security;

import ia.code.auth_service.entity.Usuario;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails {

    private final Usuario usuario;

    public String getUsername() {
        return usuario.getCorreo();
    }

    public String getPassword() {
        return usuario.getPassword();
    }

    public Integer getIdUsuario() {
        return usuario.getIdUsuario();
    }

    public boolean isEnabled() {
        return usuario.isEnabled();
    }

    public String getRole() {
        return usuario.getRoles()
                .stream()
                .findFirst()
                .map(rol -> "ROLE_" + rol.getNombre())
                .orElse("ROLE_USER");
    }
}
