package ia.code.catalog_service.usecase;

import ia.code.catalog_service.entity.Producto;
import reactor.core.publisher.Mono;

public interface ProductoUseCase {
    Mono<Producto> getProductoById(Integer idProducto);
}
