package ia.code.cart_service.controller;

import ia.code.cart_service.entity.dto.CartRequest;
import ia.code.cart_service.entity.dto.CartResponse;
import ia.code.cart_service.usecase.CartUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carrito")
public class CartController {
    private final CartUseCase cartUseCase;

    @PostMapping("/agregar")
    public Mono<ResponseEntity<CartResponse>> addItemToCart(
            @RequestHeader(value = "X-User-Id", required = false) Integer idUsuarioHeader,
            @RequestBody CartRequest cartRequest) {

        Integer finalUserId = (idUsuarioHeader != null) ? idUsuarioHeader : cartRequest.getIdUsuario();
        if (finalUserId == null) { return Mono.just(ResponseEntity.badRequest().build()); }
        cartRequest.setIdUsuario(finalUserId);
        return cartUseCase.addItemToCart(cartRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @GetMapping({"/usuario/{userId}", "/usuario"})
    public Mono<ResponseEntity<CartResponse>> getCartByUserId(
            @RequestHeader(value = "X-User-Id", required = false) Integer idUsuarioHeader,
            @PathVariable(required = false) Integer userId) {

        Integer finalUserId = (idUsuarioHeader != null) ? idUsuarioHeader : userId;
        if (finalUserId == null) { return Mono.just(ResponseEntity.badRequest().build()); }

        return cartUseCase.getCartByUserId(finalUserId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/producto/{productId}")
    public Mono<ResponseEntity<CartResponse>> removeItemFromCart(
            @RequestHeader(value = "X-User-Id", required = false) Integer idUsuarioHeader,
            @RequestParam(value = "userId", required = false) Integer idUsuarioParam,
            @PathVariable Integer productId) {

        Integer finalUserId = (idUsuarioHeader != null) ? idUsuarioHeader : idUsuarioParam;
        if (finalUserId == null) { return Mono.just(ResponseEntity.badRequest().build()); }

        return cartUseCase.removeItemFromCart(finalUserId, productId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PutMapping("/actualizar")
    public Mono<ResponseEntity<CartResponse>> updateItemQuantity(@RequestBody CartRequest cartRequest) {
        return cartUseCase.updateItemQuantity(cartRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }


    @DeleteMapping("/limpiar")
    public Mono<ResponseEntity<Void>> clearCart(@RequestHeader("X-User-Id") Integer userId) {
        return cartUseCase.clearCart(userId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
