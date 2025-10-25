package ia.code.auth_service.usecase;

import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.entity.dto.UsuarioDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UsuarioUseCase {
    Optional<UsuarioDto> getUsuarioDto(String nombre);
    Usuario getUsuario(Integer id);
    Usuario createUsuario(Usuario usuario);
}
