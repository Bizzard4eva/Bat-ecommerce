package ia.code.catalog_service.controller;

import ia.code.catalog_service.entity.Marca;
import ia.code.catalog_service.usecase.MarcaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/marcas")
@RequiredArgsConstructor
public class MarcaController {
    private final MarcaUseCase marcaUseCase;

    // obtener Marca por ID
    @GetMapping("/{idMarca}")
    public Mono<ResponseEntity<Marca>> findById(@PathVariable Integer idMarca){
        return marcaUseCase.findById(idMarca)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // listar todas las marcas
    @GetMapping("/listarMarcas")
    public Flux<Marca> listarMarcas(){
        return marcaUseCase.findAll();
    }

    // crear una nueva marca
    @PostMapping("/crearMarca")
    public Mono<ResponseEntity<Marca>> crearMarca(@RequestBody Marca marca){
        return marcaUseCase.save(marca)
                .map(saved -> ResponseEntity.status(201).body(saved));
    }

    // actualizar una marca existente
    @PutMapping("/actualizarMarca/{idMarca}")
    public Mono<ResponseEntity<Marca>> actualizarMarca(@PathVariable Integer idMarca, @RequestBody Marca marca){
        return marcaUseCase.update(idMarca, marca)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // eliminar una marca por su ID
    @DeleteMapping("/eliminarMarca/{idMarca}")
    public Mono<ResponseEntity<Marca>> eliminarMarca(@PathVariable Integer idMarca){
        return marcaUseCase.deleteById(idMarca)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
