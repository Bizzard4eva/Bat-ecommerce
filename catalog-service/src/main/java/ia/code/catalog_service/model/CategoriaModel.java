package ia.code.catalog_service.model;

import ia.code.catalog_service.entity.Categoria;
import ia.code.catalog_service.repository.CategoriaRepository;
import ia.code.catalog_service.usecase.CategoriaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoriaModel implements CategoriaUseCase {
    private final CategoriaRepository categoriaRepository;

    @Override
    public Mono<Categoria> findById(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Override
    public Flux<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Mono<Categoria> save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Mono<Categoria> update(Integer id, Categoria categoria) {
        return categoriaRepository.findById(id)
                .flatMap(c -> {
                    c.setNombre(categoria.getNombre());
                    return categoriaRepository.save(c);
                });
    }

    @Override
    public Mono<Void> deleteById(Integer idCategoria) {
        return categoriaRepository.deleteById(idCategoria);
    }
}
