package ia.code.cart_service.usecase;

import ia.code.cart_service.entity.dto.ActualizarItemRequest;
import ia.code.cart_service.entity.dto.AgregarItemRequest;
import ia.code.cart_service.entity.dto.CarritoDto;
import reactor.core.publisher.Mono;

public interface CarritoUseCase {
    Mono<CarritoDto> obtenerCarrito(Integer idUsuario);
    Mono<CarritoDto> agregarItem(Integer idUsuario, AgregarItemRequest request);
    Mono<CarritoDto> actualizarItem(Integer idUsuario, Integer idItem, ActualizarItemRequest request);
    Mono<CarritoDto> eliminarItem(Integer idUsuario, Integer idItem);
    Mono<Void> vaciarCarrito(Integer idUsuario);
}
