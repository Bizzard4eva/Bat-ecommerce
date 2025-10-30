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
    public Mono<ResponseEntity<CartResponse>> addItemToCart(@RequestHeader("X-User-Id") Integer idUsuario, @RequestBody CartRequest cartRequest) {
        cartRequest.setIdUsuario(idUsuario);
        return cartUseCase.addItemToCart(cartRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @GetMapping("/usuario/{userId}")
    public Mono<ResponseEntity<CartResponse>> getCart(@PathVariable Integer userId) {
        return cartUseCase.getCartByUserId(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario")
    public Mono<ResponseEntity<CartResponse>> getCartByUserId(@RequestHeader("X-User-Id") Integer userId) {
        return cartUseCase.getCartByUserId(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/usuario/{userId}/producto/{productId}")
    public Mono<ResponseEntity<CartResponse>> removeItemFromCart(@PathVariable Integer userId, @PathVariable Integer productId) {
        return cartUseCase.removeItemFromCart(userId, productId)
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

    @DeleteMapping("/usuario/{userId}/limpiar")
    public Mono<ResponseEntity<Void>> clearCart(@PathVariable Integer userId) {
        return cartUseCase.clearCart(userId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
