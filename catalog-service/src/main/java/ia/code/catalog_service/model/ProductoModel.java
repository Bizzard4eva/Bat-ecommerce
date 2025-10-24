package ia.code.catalog_service.model;

import ia.code.catalog_service.entity.Producto;
import ia.code.catalog_service.repository.ProductoRepository;
import ia.code.catalog_service.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoModel implements ProductoUseCase {

    private final ProductoRepository productoRepository;

    @Override
    public Mono<Producto> findById(Integer idProducto) {

        return productoRepository.findById(idProducto);
    }

    @Override
    public Flux<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Mono<Producto> update(Integer id, Producto producto) {
        return productoRepository.findById(id).flatMap(p -> {
            p.setNombre(producto.getNombre());
            p.setPrecio(producto.getPrecio());
            p.setDescripcion(producto.getDescripcion());
            p.setStock(producto.getStock());
            p.setIdCategoria(producto.getIdCategoria());
            p.setIdMarca(producto.getIdMarca());
            return productoRepository.save(p);
        });
    }

    @Override
    public Mono<Void> deleteById(Integer idProducto) {
        return productoRepository.deleteById(idProducto);
    }

    @Override
    public Flux<Producto> findByIdCategoria(Integer idCategoria) {
        return productoRepository.findByIdCategoria(idCategoria);
    }


}
