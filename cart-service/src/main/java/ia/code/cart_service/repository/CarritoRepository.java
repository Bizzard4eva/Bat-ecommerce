package ia.code.cart_service.repository;

import ia.code.cart_service.entity.Carrito;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CarritoRepository extends ReactiveCrudRepository<Carrito, Integer> {
    @Query("SELECT * FROM carrito WHERE id_usuario = :idUsuario")
    Mono<Carrito> findByIdUsuario(Integer idUsuario);

    Mono<Void> deleteByIdUsuario(Integer idUsuario);
}
