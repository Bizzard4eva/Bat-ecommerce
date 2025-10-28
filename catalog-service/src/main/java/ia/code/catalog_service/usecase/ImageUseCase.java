package ia.code.catalog_service.usecase;

import ia.code.catalog_service.entity.Image;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ImageUseCase {
    Mono<Image> save(Image image);
    Flux<Image> findAll();
    Mono<Image> findById(Integer idImage);
    Mono<Image> update(Integer idImage, Image image);
    Mono<Void> deleteById(Integer idImage);
}
