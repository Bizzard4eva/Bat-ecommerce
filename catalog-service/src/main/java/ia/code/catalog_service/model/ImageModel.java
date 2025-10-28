package ia.code.catalog_service.model;

import ia.code.catalog_service.entity.Image;
import ia.code.catalog_service.repository.ImageRepository;
import ia.code.catalog_service.usecase.ImageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ImageModel implements ImageUseCase {
    private final ImageRepository imageRepository;
    @Override
    public Mono<Image> save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Flux<Image> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public Mono<Image> findById(Integer idImage) {
        return imageRepository.findById(idImage);
    }

    @Override
    public Mono<Image> update(Integer idImage, Image image) {
        return imageRepository.findById(idImage)
                .flatMap(i->{
                    i.setUrl(image.getUrl());
                    return imageRepository.save(i);
                });
    }

    @Override
    public Mono<Void> deleteById(Integer idImage) {
        return imageRepository.deleteById(idImage);
    }
}
