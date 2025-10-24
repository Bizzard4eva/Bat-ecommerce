package ia.code.catalog_service.model;

import ia.code.catalog_service.entity.Producto;
import ia.code.catalog_service.repository.ProductoRepository;
import ia.code.catalog_service.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoModel implements ProductoUseCase {

    private final ProductoRepository productoRepository;

    @Override
    public Mono<Producto> getProductoById(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }
}
