package ia.code.catalog_service.usecase;

import ia.code.catalog_service.entity.Categoria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoriaUseCase {
    Mono<Categoria> findById(Integer idCategoria);

    Flux<Categoria> findAll();

    Mono<Categoria> save(Categoria categoria);

    Mono<Categoria> update(Integer id, Categoria categoria);

    Mono<Void> deleteById(Integer idCategoria);
}
