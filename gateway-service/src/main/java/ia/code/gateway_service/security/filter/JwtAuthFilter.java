package ia.code.gateway_service.security.filter;

import ia.code.gateway_service.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("🚀 GATEWAY - Processing: " + path);

        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            System.out.println("✅ GATEWAY - Public route, skipping: " + path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ GATEWAY - No valid Authorization header in: " + path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.isTokenValid(token)) {
            System.out.println("❌ GATEWAY - Token INVALID in: " + path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Token válido
        Claims claims = jwtUtil.extractAllClaims(token);
        String username = claims.getSubject();
        Integer userId = claims.get("id", Integer.class);  // ← Como INTEGER
        String role = claims.get("role", String.class);    // ← Como STRING

        System.out.println("✅ GATEWAY - Token VALID for: " + username + " (ID: " + userId + ", Role: " + role + ") in: " + path);

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("Authorization", authHeader)
                .header("X-User-Id", userId != null ? userId.toString() : "unknown") // ← Convertir a String
                .header("X-User-Name", username)
                .header("X-User-Roles", role != null ? role : "ROLE_USER")
                .build();

        System.out.println("🎯 GATEWAY - Forwarding to microservice: " + path);
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
}
