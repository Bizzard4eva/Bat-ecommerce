package ia.code.catalog_service.controller;

import ia.code.catalog_service.entity.Image;
import ia.code.catalog_service.usecase.ImageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageUseCase imageUseCase;

    // obtener Imagen por ID
    @GetMapping("/{idImage}")
    public Mono<ResponseEntity<Image>> findById(@PathVariable Integer idImage) {
        return imageUseCase.findById(idImage)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // listar todas las Imagenes
    @GetMapping("/listarImagenes")
    public Flux<Image> listarImagenes() {
        return imageUseCase.findAll();
    }

    // crear una nueva Imagen
    @PostMapping("/crearImagen")
    public Mono<ResponseEntity<Image>> crearImagen(@RequestBody Image image) {
        return imageUseCase.save(image)
                .map(saved -> ResponseEntity.status(201).body(saved));
    }

    // actualizar una Imagen existente
    @PutMapping("/actualizarImagen/{idImage}")
    public Mono<ResponseEntity<Image>> actualizarImagen(@PathVariable Integer idImage, @RequestBody Image image) {
        return imageUseCase.update(idImage, image)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // eliminar una Imagen por su ID
    @DeleteMapping("/eliminarImagen/{idImage}")
    public Mono<ResponseEntity<Image>> eliminarImagen(@PathVariable Integer idImage) {
        return imageUseCase.deleteById(idImage)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
