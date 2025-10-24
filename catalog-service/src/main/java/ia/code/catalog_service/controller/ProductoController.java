package ia.code.catalog_service.controller;

import ia.code.catalog_service.entity.Producto;
import ia.code.catalog_service.entity.dto.ProductoDto;
import ia.code.catalog_service.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;


    // Obtener un producto por su ID
    @GetMapping("/{idProducto}")
    public Mono<ResponseEntity<Producto>> findById(@PathVariable Integer idProducto) {
        return productoUseCase.findById(idProducto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Listar todos los productos
    @GetMapping("/listarProductos")
    public Flux<Producto> listarProductos() {
        return productoUseCase.findAll();
    }

    // Crear un nuevo producto
    @PostMapping("/crearProducto")
    public Mono<ResponseEntity<Producto>> crearProducto(@RequestBody Producto producto) {
        return productoUseCase.save(producto)
                .map(saved -> ResponseEntity.status(201).body(saved));
    }

    // Actualizar un producto existente
    @PutMapping("/actualizarProducto/{idProducto}")
    public Mono<ResponseEntity<Producto>> actualizarProducto(@PathVariable Integer idProducto, @RequestBody Producto producto) {
        return productoUseCase.update(idProducto, producto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Eliminar un producto por su ID
    @DeleteMapping("/eliminarProducto/{idProducto}")
    public Mono<ResponseEntity<Producto>> eliminarProducto(@PathVariable Integer idProducto) {
        return productoUseCase.deleteById(idProducto)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    // Listar productos por ID de categoría
    @GetMapping("/categoria/{idCategoria}")
    public Flux<Producto> listarProductosPorCategoria(@PathVariable Integer idCategoria) {
        return productoUseCase.findByIdCategoria(idCategoria);
    }

    // Listar todos los productos con detalles (DTO)
    // muestra la informacion de la categoria y marca asociada a cada producto
    @GetMapping("/detalles")
    public Flux<ProductoDto> listarProductosConDetalles() {
        return productoUseCase.findAllWithDetailsDto();
    }

    @GetMapping("/detalles/{idProducto}")
    public Mono<ResponseEntity<ProductoDto>> listarProductoConDetallesPorId(@PathVariable Integer idProducto) {
        return productoUseCase.findByIdWithDetailsDto(idProducto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
