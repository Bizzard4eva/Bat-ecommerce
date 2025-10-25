package ia.code.auth_service.model;

import ia.code.auth_service.entity.Rol;
import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.entity.dto.UsuarioDto;
import ia.code.auth_service.repository.RolRepository;
import ia.code.auth_service.repository.UsuarioRepository;
import ia.code.auth_service.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioModel implements UsuarioUseCase {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public Optional<UsuarioDto> getUsuarioDto(String nombre) {
        return usuarioRepository.findByNombre(nombre)
                .map(usuario -> modelMapper.map(usuario, UsuarioDto.class));
    }

    @Override
    public Usuario getUsuario(Integer id) {
        return usuarioRepository.getUsuariosByIdUsuario(id);
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEnabled(true);

        Rol rol = rolRepository.findByNombre("USER");
        usuario.getRoles().add(rol);

        return usuarioRepository.save(usuario);
    }
}
