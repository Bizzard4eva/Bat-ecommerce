package ia.code.cart_service.model;

import ia.code.cart_service.entity.Cart;
import ia.code.cart_service.entity.CartItem;
import ia.code.cart_service.entity.dto.CartItemResponse;
import ia.code.cart_service.entity.dto.CartRequest;
import ia.code.cart_service.entity.dto.CartResponse;
import ia.code.cart_service.entity.dto.ProductoDto;
import ia.code.cart_service.repository.CartItemRepository;
import ia.code.cart_service.repository.CartRepository;
import ia.code.cart_service.usecase.CartUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartModel implements CartUseCase {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final WebClient webClient;

    // Agrega un ítem al carrito del usuario
    @Override
    public Mono<CartResponse> addItemToCart(CartRequest cartRequest) {
        // Buscar el carrito del usuario o crear uno nuevo si no existe
        return cartRepository.findByIdUsuario(cartRequest.getIdUsuario())
                .switchIfEmpty(createNewCart(cartRequest.getIdUsuario()))
                .flatMap(cart -> {
                    // Verificar si el producto ya está en el carrito
                    return cartItemRepository.findByIdCartAndIdProducto(cart.getId_cart(), cartRequest.getIdProducto())
                            .flatMap(existingItem -> {
                                // Si existe, actualizar la cantidad
                                existingItem.setCantidad(existingItem.getCantidad() + cartRequest.getCantidad());
                                return updateCartItemWithProductPrice(existingItem)
                                        .flatMap(updatedItem -> cartItemRepository.save(updatedItem));
                            })
                            .switchIfEmpty(
                                    // Si no existe, crear un nuevo ítem
                                    createNewCartItem(cart.getId_cart(), cartRequest.getIdProducto(), cartRequest.getCantidad())
                            )
                            .then(updateCartTimestamp(cart.getId_cart()))
                            .then(getCartResponse(cart.getId_cart()));
                });
    }

    // Obtiene el carrito del usuario por su ID
    @Override
    public Mono<CartResponse> getCartByUserId(Integer userId) {
        return cartRepository.findByIdUsuario(userId)
                .flatMap(cart -> getCartResponse(cart.getId_cart()))
                .switchIfEmpty(Mono.just(new CartResponse())); // Devuelve un carrito vacío si no existe;
    }

    // Elimina un ítem del carrito del usuario
    @Override
    public Mono<CartResponse> removeItemFromCart(Integer userId, Integer productId) {
        return cartRepository.findByIdUsuario(userId)
                .flatMap(cart -> cartItemRepository.deleteByIdCartAndIdProducto(cart.getId_cart(), productId)
                        .then(updateCartTimestamp(cart.getId_cart()))
                        .then(getCartResponse(cart.getId_cart())))
                .switchIfEmpty(Mono.just(new CartResponse())); // Si no hay carrito, devuelve vacío
    }

    // Actualiza la cantidad de un ítem en el carrito del usuario
    @Override
    public Mono<CartResponse> updateItemQuantity(CartRequest cartRequest) {
        return cartRepository.findByIdUsuario(cartRequest.getIdUsuario())
                .flatMap(cart -> cartItemRepository.findByIdCartAndIdProducto(cart.getId_cart(), cartRequest.getIdProducto())
                        .flatMap(item -> {
                            item.setCantidad(cartRequest.getCantidad());
                            return updateCartItemWithProductPrice(item)
                                    .flatMap(updatedItem -> cartItemRepository.save(updatedItem));
                        })
                        .then(updateCartTimestamp(cart.getId_cart()))
                        .then(getCartResponse(cart.getId_cart())))
                .switchIfEmpty(Mono.just(new CartResponse()));
    }

    // Limpia todos los ítems del carrito del usuario
    @Override
    public Mono<Void> clearCart(Integer userId) {
        return cartRepository.findByIdUsuario(userId)
                .flatMap(cart -> cartItemRepository.findByIdCart(cart.getId_cart())
                        .flatMap(item -> cartItemRepository.deleteById(item.getId_item()))
                        .then()
                        .then(updateCartTimestamp(cart.getId_cart()))).then();
    }

    // Metodos auxiliares

    // Crea un nuevo carrito para el usuario dado
    private Mono<Cart> createNewCart(Integer userId) {
        Cart newCart = Cart.builder()
                .id_usuario(userId)
                .fecha_creacion(LocalDateTime.now())
                .fecha_actualizacion(LocalDateTime.now())
                .build();
        return cartRepository.save(newCart);
    }

    // Crea un nuevo item en el carrito
    private Mono<CartItem> createNewCartItem(Integer cartId, Integer productId, Integer cantidad) {
        return getProductoById(productId)
                .flatMap(producto -> {
                    CartItem newItem = new CartItem();
                    newItem.setId_cart(cartId);
                    newItem.setId_producto(productId);
                    newItem.setCantidad(cantidad);
                    newItem.setPrecio_unitario(producto.getPrecio());
                    newItem.setSubtotal(producto.getPrecio() * cantidad);
                    return cartItemRepository.save(newItem);
                });
    }

    // Actualiza el precio unitario y subtotal del item del carrito basado en el precio actual del producto
    private Mono<CartItem> updateCartItemWithProductPrice(CartItem cartItem) {
        return getProductoById(cartItem.getId_producto())
                .map(producto -> {
                    cartItem.setPrecio_unitario(producto.getPrecio());
                    cartItem.setSubtotal(producto.getPrecio() * cartItem.getCantidad());
                    return cartItem;
                });
    }

    // Actualiza la fecha de actualizacion del carrito
    private Mono<Cart> updateCartTimestamp(Integer cartId) {
        return cartRepository.findById(cartId)
                .flatMap(cart -> {
                    cart.setFecha_actualizacion(LocalDateTime.now());
                    return cartRepository.save(cart);
                });
    }

    // Construye la respuesta del carrito con los items y el total
    private Mono<CartResponse> getCartResponse(Integer cartId) {
        return cartRepository.findById(cartId)
                .flatMap(cart -> cartItemRepository.findByIdCart(cartId)
                        .collectList()
                        .flatMap(items -> {
                            if (items.isEmpty()) {
                                return Mono.just(new CartResponse(cart.getId_cart(), cart.getId_usuario(),
                                        cart.getFecha_creacion(), cart.getFecha_actualizacion(), List.of(), 0.0));
                            }
                            return Flux.fromIterable(items)
                                    .flatMap(item -> getProductoById(item.getId_producto())
                                            .map(producto -> {
                                                Double subtotal = producto.getPrecio() * item.getCantidad();
                                                return new CartItemResponse(item.getId_item(), item.getId_producto(),
                                                        producto.getNombre(), producto.getPrecio(),
                                                        item.getCantidad(), subtotal);
                                            }))
                                    .collectList()
                                    .map(itemResponses -> {
                                        Double total = itemResponses.stream()
                                                .mapToDouble(CartItemResponse::getSubtotal)
                                                .sum();
                                        return new CartResponse(cart.getId_cart(), cart.getId_usuario(),
                                                cart.getFecha_creacion(), cart.getFecha_actualizacion(),
                                                itemResponses, total);
                                    });
                        }));
    }

    // Obtiene los detalles del producto desde el servicio de productos
    private Mono<ProductoDto> getProductoById(Integer idProducto) {
        return webClient.get()
                .uri("/productos/detalles/{id}", idProducto)
                .retrieve()
                .bodyToMono(ProductoDto.class);
    }
}
