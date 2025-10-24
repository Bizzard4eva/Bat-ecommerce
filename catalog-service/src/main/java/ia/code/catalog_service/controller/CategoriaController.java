package ia.code.catalog_service.controller;

import ia.code.catalog_service.entity.Categoria;
import ia.code.catalog_service.usecase.CategoriaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaUseCase categoriaUseCase;

    // Obtener Categoria por ID
    @GetMapping("{idCategoria}")
    public Mono<ResponseEntity<Categoria>> findById(@PathVariable Integer idCategoria) {
        return categoriaUseCase.findById(idCategoria)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Listar todos las categorías
    @GetMapping("/listarCategorias")
    public Flux<Categoria> listarCategorias() {
        return categoriaUseCase.findAll();
    }

    // Crear una nueva categoría
    @PostMapping("/crearCategoria")
    public Mono<ResponseEntity<Categoria>> crearCategoria(@RequestBody Categoria categoria) {
        return categoriaUseCase.save(categoria)
                .map(saved -> ResponseEntity.status(201).body(saved));
    }

    // Actualizar una categoría existente
    @PutMapping("/actualizarCategoria/{idCategoria}")
    public Mono<ResponseEntity<Categoria>> actualizarCategoria(@PathVariable Integer idCategoria, @RequestBody Categoria categoria) {
        return categoriaUseCase.update(idCategoria, categoria)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Eliminar una categoría por su ID
    @DeleteMapping("/eliminarCategoria/{idCategoria}")
    public Mono<ResponseEntity<Categoria>> eliminarCategoria(@PathVariable Integer idCategoria) {
        return categoriaUseCase.deleteById(idCategoria)
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}
