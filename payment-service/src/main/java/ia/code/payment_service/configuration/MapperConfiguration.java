package ia.code.payment_service.configuration;

import ia.code.payment_service.entity.Enum.PaymentMethod;
import ia.code.payment_service.entity.Enum.PaymentStatus;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addConverter(ctx ->
                PaymentMethod.valueOf(ctx.getSource().toUpperCase()),
                String.class,
                PaymentMethod.class);
        mapper.addConverter(ctx ->
                        ctx.getSource() == null ? null : ctx.getSource().name(),
                PaymentMethod.class,
                String.class);
        mapper.addConverter(ctx ->
                        ctx.getSource() == null ? null : ctx.getSource().name(),
                PaymentStatus.class,
                String.class);
        return mapper;
    }
}
