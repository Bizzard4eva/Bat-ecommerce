package ia.code.catalog_service.controller;

import ia.code.catalog_service.entity.Producto;
import ia.code.catalog_service.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;

    @GetMapping("/{id}")
    public Mono<Producto> getProductoById(@PathVariable Integer id) {
        return productoUseCase.getProductoById(id);
    }
}
