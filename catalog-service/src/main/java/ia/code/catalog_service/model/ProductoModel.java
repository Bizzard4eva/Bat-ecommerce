package ia.code.catalog_service.model;

import ia.code.catalog_service.entity.Producto;
import ia.code.catalog_service.entity.dto.ProductoDto;
import ia.code.catalog_service.repository.ProductoRepository;
import ia.code.catalog_service.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoModel implements ProductoUseCase {


    private final ProductoRepository productoRepository;
    private final DatabaseClient databaseClient;

    @Override
    public Mono<Producto> findById(Integer idProducto) {

        return productoRepository.findById(idProducto);
    }

    @Override
    public Flux<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Mono<Producto> update(Integer id, Producto producto) {
        return productoRepository.findById(id).flatMap(p -> {
            p.setNombre(producto.getNombre());
            p.setPrecio(producto.getPrecio());
            p.setDescripcion(producto.getDescripcion());
            p.setStock(producto.getStock());
            p.setIdCategoria(producto.getIdCategoria());
            p.setIdMarca(producto.getIdMarca());
            return productoRepository.save(p);
        });
    }

    @Override
    public Mono<Void> deleteById(Integer idProducto) {
        return productoRepository.deleteById(idProducto);
    }

    @Override
    public Flux<Producto> findByIdCategoria(Integer idCategoria) {
        return productoRepository.findByIdCategoria(idCategoria);
    }

    // DTO methods

    @Override
    public Flux<ProductoDto> findAllWithDetailsDto() {
                String sql = """
            SELECT p.id_producto, p.nombre, p.precio, p.descripcion,
                   c.nombre AS nombre_categoria,
                   m.nombre AS nombre_marca
            FROM producto p
            INNER JOIN categoria c ON p.id_categoria = c.id_categoria
            INNER JOIN marca m ON p.id_marca = m.id_marca
            ORDER BY p.id_producto ASC
        """;

        return databaseClient.sql(sql)
                .map((row, meta) -> {
                    ProductoDto dto = new ProductoDto();
                    dto.setId_producto(row.get("id_producto", Integer.class));
                    dto.setNombre(row.get("nombre", String.class));
                    dto.setPrecio(row.get("precio", Double.class));
                    dto.setDescripcion(row.get("descripcion", String.class));
                    dto.setNombreCategoria(row.get("nombre_categoria", String.class));
                    dto.setNombreMarca(row.get("nombre_marca", String.class));
                    return dto;
                })
                .all();

    }

    @Override
    public Mono<ProductoDto> findByIdWithDetailsDto(Integer idProducto) {
        return databaseClient.sql("""
            SELECT p.id_producto, p.nombre, p.precio, p.descripcion,
                   c.nombre AS nombre_categoria,
                   m.nombre AS nombre_marca
            FROM producto p
            INNER JOIN categoria c ON p.id_categoria = c.id_categoria
            INNER JOIN marca m ON p.id_marca = m.id_marca
            WHERE p.id_producto = $1
            """)
            .bind(0, idProducto)  // bind por índice
            .map((row, metadata) -> new ProductoDto(
                    row.get("id_producto", Integer.class),
                    row.get("nombre", String.class),
                    row.get("precio", Double.class),
                    row.get("descripcion", String.class),
                    row.get("nombre_categoria", String.class),
                    row.get("nombre_marca", String.class)
            ))
            .one();
    }


}
