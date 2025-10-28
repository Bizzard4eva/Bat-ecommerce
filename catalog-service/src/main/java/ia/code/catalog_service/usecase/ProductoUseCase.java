package ia.code.catalog_service.usecase;

import ia.code.catalog_service.entity.Producto;
import ia.code.catalog_service.entity.dto.ProductoDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoUseCase {
    Mono<Producto> findById(Integer idProducto);

    Flux<Producto> findAll();

    Mono<Producto> save(Producto producto);

    Mono<Producto> update(Integer id, Producto producto);

    Mono<Void> deleteById(Integer idProducto);

    Flux<Producto> findByIdCategoria(Integer idCategoria);

    // DTO method
    Flux<ProductoDto> findAllWithDetailsDto();

    Mono<ProductoDto> findByIdWithDetailsDto(Integer idProducto);
}
