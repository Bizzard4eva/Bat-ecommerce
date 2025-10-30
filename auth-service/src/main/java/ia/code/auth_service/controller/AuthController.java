package ia.code.auth_service.controller;

import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.entity.dto.*;
import ia.code.auth_service.security.JwtUtil;
import ia.code.auth_service.service.UsuarioService;
import ia.code.auth_service.usecase.UsuarioUseCase;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioUseCase usuarioUseCase;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody Usuario usuario) {
        RegisterResponse nuevoUsuario = modelMapper.map(usuarioUseCase.createUsuario(usuario), RegisterResponse.class);
        return ResponseEntity.status(201).body(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest request) {
        try {
            Usuario usuario = usuarioUseCase.getUserByCorreo(request.getCorreo())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
                throw new RuntimeException("Credenciales inválidas");
            }
            if (!usuario.isEnabled()) {
                throw new RuntimeException("Usuario deshabilitado");
            }

            String role = usuario.getRoles()
                    .stream()
                    .findFirst()
                    .map(rol -> "ROLE_" + rol.getNombre())
                    .orElse("ROLE_USER");
            String token = jwtUtil.generateToken(
                    usuario.getCorreo(),
                    role,
                    usuario.getIdUsuario()
            );

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> GetProfile(@RequestHeader("X-User-Name") String username) {
        ProfileResponse usuario = modelMapper.map(usuarioService.getUserByCorreo(username), ProfileResponse.class);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> UpdateProfile(@RequestHeader("X-User-Name") String username,
                                           @RequestBody ProfileUpdateRequest request) {
        Usuario usuario = usuarioUseCase.getUserByCorreo(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(request.getNombre());
        usuario.setCelular(request.getCelular());
        usuario.setDireccion(request.getDireccion());

        ProfileResponse actualizado = modelMapper.map(usuarioUseCase.updateUsuario(usuario), ProfileResponse.class);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> DeleteProfile(@RequestHeader("X-User-Id") Integer id) {
        usuarioUseCase.deleteUsuario(id);
        return ResponseEntity.ok("Usuario" + id + " Eliminado");
    }
    // TEST
    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("No token provided");
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String user = claims.getSubject();
            Integer userId = claims.get("id", Integer.class);
            String role = claims.get("role", String.class);

            return ResponseEntity.ok("Token válido - Usuario: " + user + ", ID: " + userId + ", Rol: " + role);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido: " + e.getMessage());
        }
    }
}




























