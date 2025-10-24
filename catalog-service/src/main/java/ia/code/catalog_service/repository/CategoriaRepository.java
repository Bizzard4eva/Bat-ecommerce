package ia.code.catalog_service.repository;

import ia.code.catalog_service.entity.Categoria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CategoriaRepository extends ReactiveCrudRepository<Categoria, Integer> {

}
