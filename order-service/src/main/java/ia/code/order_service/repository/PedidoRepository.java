package ia.code.order_service.repository;

import ia.code.order_service.entity.Pedido;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PedidoRepository extends ReactiveCrudRepository<Pedido, Integer> {
    Flux<Pedido> findByIdUsuario(Integer idUsuario);
}
