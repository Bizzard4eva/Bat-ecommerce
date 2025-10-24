package ia.code.auth_service.controller;

import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.entity.dto.UsuarioDto;
import ia.code.auth_service.model.UsuarioModel;
import ia.code.auth_service.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    @GetMapping("/{nombre}")
    public ResponseEntity<?> getUsuarioDto(@PathVariable String nombre) {
        return usuarioUseCase.getUsuarioDto(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioUseCase.createUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
}
