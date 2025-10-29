package ia.code.auth_service.service;

import ia.code.auth_service.entity.Rol;
import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.repository.RolRepository;
import ia.code.auth_service.repository.UsuarioRepository;
import ia.code.auth_service.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioUseCase {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public Usuario createUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEnabled(true);

        Rol rol = rolRepository.findByNombre("USER");
        usuario.getRoles().add(rol);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario updateUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> getUserByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
