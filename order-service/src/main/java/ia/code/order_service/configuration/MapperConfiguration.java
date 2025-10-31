package ia.code.order_service.configuration;

import ia.code.order_service.entity.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class MapperConfiguration {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}