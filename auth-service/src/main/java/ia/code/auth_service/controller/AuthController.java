package ia.code.auth_service.controller;

import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.entity.dto.LoginRequest;
import ia.code.auth_service.entity.dto.LoginResponse;
import ia.code.auth_service.entity.dto.UsuarioDto;
import ia.code.auth_service.repository.UsuarioRepository;
import ia.code.auth_service.security.CustomUserDetails;
import ia.code.auth_service.security.JwtUtil;
import ia.code.auth_service.security.service.CustomUserDetailsService;
import ia.code.auth_service.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioUseCase usuarioUseCase;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority(),
                userDetails.getIdUsuario()
        );
        return ResponseEntity.ok(new LoginResponse(token));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        Usuario nuevoUsurio = usuarioUseCase.createUsuario(usuario);
        return ResponseEntity.status(201).body(nuevoUsurio);
    }
    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        if(jwtUtil.isTokenValid(token)) {
            String user = jwtUtil.extractAllClaims(token).getSubject();
            return ResponseEntity.ok("Token valido: " + user);
        }
        return ResponseEntity.status(401).body("Token invalido");
    }
}
