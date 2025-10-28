package ia.code.catalog_service.repository;

import ia.code.catalog_service.entity.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ImageRepository extends ReactiveCrudRepository<Image,Integer> {
    Integer id(int id);
}
