package ia.code.catalog_service.repository;

import ia.code.catalog_service.entity.Producto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Map;

@Repository
public interface ProductoRepository extends ReactiveCrudRepository<Producto, Integer> {

    Flux<Producto> findByIdCategoria(Integer idCategoria);

}
