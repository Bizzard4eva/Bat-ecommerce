package ia.code.auth_service.configuration;

import ia.code.auth_service.entity.Rol;
import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.repository.RolRepository;
import ia.code.auth_service.repository.UsuarioRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.count() == 0) { crearRoles(); }
        if (usuarioRepository.count() == 0) { crearUsuarios(); }
        //TODO
    }

    private void crearRoles() {
        Rol user = Rol.builder().nombre("USER").build();
        Rol admin = Rol.builder().nombre("ADMIN").build();
        rolRepository.saveAll(List.of(user, admin));

        System.out.println("✅ Roles creados: ROLE_USER y ROLE_ADMIN!");
    }

    private void crearUsuarios() {
        Rol adminRole = rolRepository.findByNombre("ADMIN");

        Usuario admin = Usuario.builder()
                .nombre("ADMIN")
                .correo("admin@gmail.com")
                .celular("999888777")
                .direccion("Confidendial .l.")
                .password(passwordEncoder.encode("admin"))
                .enabled(true)
                .roles(Set.of(adminRole))
                .build();
        usuarioRepository.saveAll(List.of(admin));

        System.out.println("✅ Usuario Admin creado!");
    }
};
