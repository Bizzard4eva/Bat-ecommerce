package ia.code.order_service.repository;

import ia.code.order_service.entity.DetallePedido;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends ReactiveCrudRepository<DetallePedido, Integer> {

}
