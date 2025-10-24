package ia.code.catalog_service.model;

import ia.code.catalog_service.entity.Marca;
import ia.code.catalog_service.repository.MarcaRepository;
import ia.code.catalog_service.usecase.MarcaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MarcaModel implements MarcaUseCase {
    private final MarcaRepository marcaRepository;

    @Override
    public Mono<Marca> save(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public Flux<Marca> findAll() {
        return marcaRepository.findAll();
    }

    @Override
    public Mono<Marca> findById(Integer idMarca) {
        return marcaRepository.findById(idMarca);
    }

    @Override
    public Mono<Marca> update(Integer id, Marca marca) {
        return marcaRepository.findById(id)
                .flatMap(m -> {
                    m.setNombre(marca.getNombre());
                    return marcaRepository.save(m);
                });
    }

    @Override
    public Mono<Void> deleteById(Integer idMarca) {
        return marcaRepository.deleteById(idMarca);
    }
}
