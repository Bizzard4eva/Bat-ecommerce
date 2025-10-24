package ia.code.auth_service.usecase;

import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.entity.dto.UsuarioDto;

import java.util.Optional;

public interface UsuarioUseCase {
    Optional<UsuarioDto> getUsuarioDto(String nombre);
    Usuario createUsuario(Usuario usuario);
}
