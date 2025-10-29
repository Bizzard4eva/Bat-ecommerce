package ia.code.auth_service.configuration;

import ia.code.auth_service.entity.Rol;
import ia.code.auth_service.entity.Usuario;
import ia.code.auth_service.entity.dto.ProfileResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class MapperConfiguration {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Conversión personalizada para roles
        Converter<Set<Rol>, Set<String>> rolesConverter = ctx ->
                ctx.getSource() == null ? null :
                        ctx.getSource().stream()
                                .map(Rol::getNombre)
                                .collect(Collectors.toSet());

        mapper.typeMap(Usuario.class, ProfileResponse.class)
                .addMappings(m -> m.using(rolesConverter).map(Usuario::getRoles, ProfileResponse::setRoles));

        return mapper;
    }
}
