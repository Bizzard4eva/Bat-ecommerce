package ia.code.catalog_service.usecase;

import ia.code.catalog_service.entity.Marca;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarcaUseCase {
    Mono<Marca> save(Marca marca);

    Flux<Marca> findAll();

    Mono<Marca> findById(Integer idMarca);

    Mono<Marca> update(Integer id, Marca marca);

    Mono<Void> deleteById(Integer idMarca);

}
