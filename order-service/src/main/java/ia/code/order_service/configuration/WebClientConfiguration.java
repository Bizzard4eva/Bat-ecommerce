package ia.code.order_service.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(@LoadBalanced WebClient.Builder builder) {
        return builder
                .baseUrl("http://cart-service")
                .defaultHeader("X-Internal-Call", "true")
                .build();
    }

}
