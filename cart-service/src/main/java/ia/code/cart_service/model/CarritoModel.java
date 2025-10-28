package ia.code.cart_service.model;

import ia.code.cart_service.entity.Carrito;
import ia.code.cart_service.entity.dto.ActualizarItemRequest;
import ia.code.cart_service.entity.dto.AgregarItemRequest;
import ia.code.cart_service.entity.dto.CarritoDto;
import ia.code.cart_service.entity.dto.ItemCarritoDto;
import ia.code.cart_service.repository.CarritoRepository;
import ia.code.cart_service.repository.ItemCarritoRepository;
import ia.code.cart_service.usecase.CarritoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarritoModel implements CarritoUseCase {
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final DatabaseClient databaseClient;

    @Override
    public Mono<CarritoDto> obtenerCarrito(Integer idUsuario) {
        return carritoRepository.findByIdUsuario(idUsuario)
                .switchIfEmpty(crearCarritoVacio(idUsuario))
            .flatMap(this::convertirACarritoDto);
    }

    @Override
    public Mono<CarritoDto> agregarItem(Integer idUsuario, AgregarItemRequest request) {
        return null;
    }

    @Override
    public Mono<CarritoDto> actualizarItem(Integer idUsuario, Integer idItem, ActualizarItemRequest request) {
        return null;
    }

    @Override
    public Mono<CarritoDto> eliminarItem(Integer idUsuario, Integer idItem) {
        return null;
    }

    @Override
    public Mono<Void> vaciarCarrito(Integer idUsuario) {
        return null;
    }

    // Métodos auxiliares
    private Mono<Carrito> crearCarritoVacio(Integer idUsuario) {
        Carrito carrito = new Carrito();
        carrito.setIdUsuario(idUsuario);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setFechaActualizacion(LocalDateTime.now());
        carrito.setTotal(0.0);
        return carritoRepository.save(carrito);
    }

    private Mono<Void> actualizarTotalCarrito(Integer idCarrito) {
        String sql = """
            UPDATE carrito 
            SET total = (
                SELECT COALESCE(SUM(subtotal), 0) 
                FROM item_carrito 
                WHERE id_carrito = $1
            ),
            fecha_actualizacion = $2
            WHERE id_carrito = $1
        """;

        return databaseClient.sql(sql)
            .bind(0, idCarrito)
            .bind(1, LocalDateTime.now())
            .then();
    }

    private Mono<Double> obtenerPrecioProducto(Integer idProducto) {
        // En un escenario real, aquí llamarías al catalog-service
        // Por ahora, simulamos obteniendo un precio fijo
        String sql = "SELECT precio FROM producto WHERE id_producto = $1";

        return databaseClient.sql(sql)
            .bind(0, idProducto)
            .map((row, metadata) -> row.get("precio", Double.class))
            .one()
            .defaultIfEmpty(0.0);
    }

    private Mono<CarritoDto> convertirACarritoDto(Carrito carrito) {
        CarritoDto dto = new CarritoDto();
        dto.setId_carrito(carrito.getId());
        dto.setIdUsuario(carrito.getIdUsuario());
        dto.setFechaCreacion(carrito.getFechaCreacion());
        dto.setFechaActualizacion(carrito.getFechaActualizacion());
        dto.setTotal(carrito.getTotal());

        return obtenerItemsCarrito(carrito.getId())
            .collectList()
            .map(items -> {
                dto.setItems(items);
                return dto;
            });
    }

    private Flux<ItemCarritoDto> obtenerItemsCarrito(Integer idCarrito) {
        String sql = """
            SELECT ic.*, p.nombre as nombre_producto, i.url as imagen_url
            FROM item_carrito ic
            LEFT JOIN producto p ON ic.id_producto = p.id_producto
            LEFT JOIN image i ON p.id_img = i.id_img
            WHERE ic.id_carrito = $1
        """;

        return databaseClient.sql(sql)
            .bind(0, idCarrito)
            .map((row, metadata) -> {
                ItemCarritoDto dto = new ItemCarritoDto();
                dto.setId_item(row.get("id_item", Integer.class));
                dto.setIdProducto(row.get("id_producto", Integer.class));
                dto.setNombreProducto(row.get("nombre_producto", String.class));
                dto.setPrecio(row.get("precio_unitario", Double.class));
                dto.setCantidad(row.get("cantidad", Integer.class));
                dto.setSubtotal(row.get("subtotal", Double.class));
                dto.setImagenUrl(row.get("imagen_url", String.class));
                return dto;
            })
            .all();
    }
}
