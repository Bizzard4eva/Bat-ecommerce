package ia.code.cart_service.repository;

import ia.code.cart_service.entity.CartItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CartItemRepository extends ReactiveCrudRepository<CartItem,Integer> {
    Flux<CartItem> findByIdCart(Integer idCart);
    Mono<Void> deleteByIdCartAndIdProducto(Integer idCart, Integer idProducto);
    Mono<CartItem> findByIdCartAndIdProducto(Integer idCart, Integer idProducto);
}
