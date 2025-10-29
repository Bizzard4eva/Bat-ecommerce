package ia.code.auth_service.usecase;

import ia.code.auth_service.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UsuarioUseCase {
    Usuario createUsuario(Usuario usuario);
    Usuario updateUsuario(Usuario usuario);
    void deleteUsuario(Integer id);
    Optional<Usuario> getUserByCorreo(String correo);
}
