package ia.code.cart_service.repository;

import ia.code.cart_service.entity.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CartRepository extends ReactiveCrudRepository<Cart,Integer> {
    Mono<Cart> findByIdUsuario(Integer idUsuario);
}
