package ia.code.cart_service.repository;

import ia.code.cart_service.entity.ItemCarrito;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemCarritoRepository extends ReactiveCrudRepository<ItemCarrito, Integer> {
    @Query("SELECT * FROM item_carrito WHERE id_carrito = :idCarrito")
    Flux<ItemCarrito> findByIdCarrito(Integer idCarrito);

    @Query("DELETE FROM item_carrito WHERE id_carrito = :idCarrito")
    Mono<Void> deleteByIdCarrito(Integer idCarrito);

    @Query("SELECT * FROM item_carrito WHERE id_carrito = :idCarrito AND id_producto = :idProducto")
    Mono<ItemCarrito> findByIdCarritoAndIdProducto(Integer idCarrito, Integer idProducto);
}
