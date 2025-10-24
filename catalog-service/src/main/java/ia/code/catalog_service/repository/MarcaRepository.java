package ia.code.catalog_service.repository;

import ia.code.catalog_service.entity.Marca;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MarcaRepository extends ReactiveCrudRepository<Marca,Integer> {
}
