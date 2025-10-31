package ia.code.cart_service.usecase;

import ia.code.cart_service.entity.dto.CartRequest;
import ia.code.cart_service.entity.dto.CartResponse;
import reactor.core.publisher.Mono;

public interface CartUseCase {
    Mono<CartResponse> addItemToCart(CartRequest cartRequest);

    Mono<CartResponse> getCartByUserId(Integer userId);

    Mono<CartResponse> removeItemFromCart(Integer userId, Integer productId);

    Mono<CartResponse> updateItemQuantity(CartRequest cartRequest);

    Mono<Void> clearCart(Integer userId);
}
