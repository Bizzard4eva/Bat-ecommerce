package ia.code.auth_service.model;

import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.entity.dto.UsuarioDto;
import ia.code.auth_service.repository.UsuarioRepository;
import ia.code.auth_service.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioModel implements UsuarioUseCase {

    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Optional<UsuarioDto> getUsuarioDto(String nombre) {
        return usuarioRepository.findByNombre(nombre)
                .map(usuario -> modelMapper.map(usuario, UsuarioDto.class));
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
